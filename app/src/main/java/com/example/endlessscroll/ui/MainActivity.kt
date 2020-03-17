package com.example.endlessscroll.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.endlessscroll.EventAdapter
import com.example.endlessscroll.R
import com.example.endlessscroll.models.Event
import com.example.endlessscroll.networking.RetrofitFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.toast
import java.io.File

class MainActivity : AppCompatActivity() {

    val eventList : MutableList<Event> = mutableListOf()

    lateinit var layoutManager : LinearLayoutManager
    lateinit var eventAdapter: EventAdapter
    lateinit var recycler: RecyclerView

    var page = 1
    var nextPage = 1
    var isLoading = false
    var pastVisiblesItems = 0
    var visibleItemCount:Int = 0
    var totalItemCount:Int = 1
    var previous_total = 0

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler = findViewById(R.id.recyclerView)
        layoutManager = LinearLayoutManager(this)
        eventAdapter = EventAdapter(eventList, this)

        recycler.layoutManager = layoutManager
        recycler.adapter = eventAdapter

//        for (i in 0..20) {
//            eventList.add(i, i.toString())
//            eventAdapter.notifyDataSetChanged()
//        }

        //loadEventInfo(page)

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                visibleItemCount = layoutManager.childCount
                totalItemCount = layoutManager.itemCount
                pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()

                if (dy>0) {

//                    if (isLoading) {
//                        if (totalItemCount > previous_total) {
//                            isLoading = false
//                            previous_total = totalItemCount
//                        }
//                    }

                    if (!isLoading && ((visibleItemCount + pastVisiblesItems) >= totalItemCount)) {
                        if (nextPage > page) {
                            loadEventInfo(nextPage)
                            toast("Now I load Events")
                        }
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
//
        buttonSome.setOnClickListener{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else{
                    //permission already granted
                    pickImageFromGallery();
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery();
            }
//            if (isStoragePermissionGranted()) {
//
//            }
//            toast(eventList.size.toString())
//            eventList.clear()
//            eventAdapter.notifyDataSetChanged()
        }
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null && data.data != null) {
            val selectedImage: Uri = data!!.data!!
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(selectedImage, filePathColumn, null, null, null) ?: return
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val filePath = cursor.getString(columnIndex)
            cursor.close()
            val file = File(filePath)
            val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
            val body = MultipartBody.Part.createFormData("file", file.name, reqFile)
            val service = RetrofitFactory.makeRetrofitService()

            CoroutineScope(Dispatchers.IO).launch {
                val response = service.postMedia(
                    multipartBody = body
                )
                val res = response.code()
                withContext(Dispatchers.Main) {
                    toast("${res}")
                    Log.e("MEDIA", "${response.code()}")
                }
            }
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        val mimeTypes = arrayListOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun addEvents (newData: MutableList<Event>) {
        Log.e("CurrentEvent", "HI, IM THERE")
        for (i in 0..(newData.size - 1)) {
            this.eventList.add(newData[i])
            Log.e("CurrentEvent", "${newData[i]}")
        }
        if (::eventAdapter.isInitialized) {
            eventAdapter.notifyDataSetChanged()
        } else {
            eventAdapter = EventAdapter(eventList, this)
            recycler.adapter
        }

    }

    private fun loadEventInfo(page: Int) {
        isLoading = true
        val service = RetrofitFactory.makeRetrofitService()
        progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getEventsByPage(page)

                if (response.isSuccessful && response.code() == 200) {

                    withContext(Dispatchers.Main) {

                        val currentEvent = response.body()?.data
                        val links = response.body()?.links
                        val meta = response.body()?.meta

                        addEvents(currentEvent!!)

                        /*if (currentEvent?.size != 0) {
                            for (i in 0..(currentEvent?.size!! - 1)) {
                                eventList.add(currentEvent[i])
                            }
                        }

                        eventAdapter.notifyDataSetChanged()*/
                        this@MainActivity.page = meta?.current_page!!

                        if (links?.next != null ) {
                            nextPage = links.next.toString().takeLast(1).toInt()
                        }

                        Log.e("CurrentEvent", "Next page: $nextPage")
                        Log.e("CurrentEvent", "Current page:  ${this@MainActivity.page}")
                        Log.e("CurrentEvent", "Links: $links")
                        Log.e("CurrentEvent", "Data: $currentEvent")
                        Log.e("CurrentEvent", "Meta: $meta")

                        //Loading ends...
                        isLoading = false
                        progressBar.visibility = View.GONE
                    }
                } else {
                    isLoading = false
                    progressBar.visibility = View.GONE
                    if (response.code() == 400) {
                        Log.e("Event","Something wrong with server/internet connection!")
                    }
                }
            } catch (e : Throwable) {
                Log.e("CurrentEvent", e.localizedMessage ?: "Error messages")
                isLoading = false
                progressBar.visibility = View.GONE
            }
            Log.e("CurrentEvent1", "$nextPage")
            Log.e("CurrentEvent1", "${eventList.size}")
        }
    }

    fun showToast(text: String) {
        GlobalScope.launch {
            Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT).show()
        }
    }

}


