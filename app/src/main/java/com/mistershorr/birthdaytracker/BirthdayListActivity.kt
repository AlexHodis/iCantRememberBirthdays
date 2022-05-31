package com.mistershorr.birthdaytracker


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.mistershorr.birthdaytracker.databinding.ActivityBirthdayListBinding
import com.backendless.persistence.DataQueryBuilder




class BirthdayListActivity : AppCompatActivity() {

    lateinit var binding : ActivityBirthdayListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBirthdayListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadDataFromBackendless()

        //make an onClickListener for the floating action button
        //that launches the DetailActivity
        binding.fabBirthdayListCreateNew.setOnClickListener {
            //make the intent
            val detailIntent = Intent(this, BirthdayDetailActivity::class.java)
            //launch the activity
            startActivity(detailIntent)
        }
    }

    override fun onStart() {
        super.onStart()

        //load the data from the database
        //put it into the recyclerview
        //this is in onStart
        loadDataFromBackendless()
    }

    private fun loadDataFromBackendless() {
        // retrieve only objects whose ownerId matches the user's objectId
        val objectId = Backendless.UserService.CurrentUser().objectId
        val whereClause = "ownerId = '$objectId'"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.whereClause = whereClause

        //this retrieves all objects regardless of owner
        // bu adding the queryBuilder will now search with the whereClause


        Backendless.Data.of(Person::class.java).find(queryBuilder,object : AsyncCallback<List<Person>?>{
            override fun handleResponse(foundPeople : List<Person>?){
                //all person instances have been found
                Log.d("BirthdayList", "handleResponse: ${foundPeople}")
                val adapter = BirthdayAdapter((foundPeople?: listOf()))
                binding.recyclerViewPeople.adapter = adapter
                binding.recyclerViewPeople.layoutManager = LinearLayoutManager(this@BirthdayListActivity)
            }

            override fun handleFault(fault: BackendlessFault){
                //an error has occurred, the error code can be retrieved with fault.getCode()
                Log.d("BirthdayList", "handleFault: ${fault.message}")
            }
        })
    }
}