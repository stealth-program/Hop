package com.hop

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_beer.*
import android.support.v4.app.ActivityCompat
import android.view.View
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import java.io.ByteArrayOutputStream
import com.hop.R.id.imageView
import android.graphics.BitmapFactory
import android.content.Intent
import android.content.pm.PackageManager
import android.support.annotation.NonNull
import java.io.FileNotFoundException



class AddBeerActivity : AppCompatActivity() {
    var id = 0
    val REQUEST_CODE_GALLERY = 999

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_beer)

        this.title = "Beer"

        try {
            val bundle: Bundle = intent.extras
            id = bundle.getInt("MainActId", 0)
            if (id != 0) {
                edtName.setText(bundle.getString("MainActName"))
                edtDate.setText(bundle.getString("MainActDate"))
                edtStyle.setText(bundle.getString("MainActStyle"))
                edtBrewery.setText(bundle.getString("MainActBrewery"))
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(bundle.getByteArray("MainActImage"), 0, bundle.getByteArray("MainActImage").size))
            }
        } catch (ex: Exception) {
        }

        btChoose.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                ActivityCompat.requestPermissions(
                        this@AddBeerActivity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_CODE_GALLERY
                )
            }
        })

        btAdd.setOnClickListener {
            val dbManager = DBManager(this)

            val values = ContentValues()
            values.put("Name", edtName.text.toString())
            values.put("Date", edtDate.text.toString())
            values.put("Style", edtStyle.text.toString())
            values.put("Brewery", edtBrewery.text.toString())
            values.put("Image", imageViewToByte(imageView))

            if (id == 0) {
                val mID = dbManager.insert(values)

                if (mID > 0) {
                    Toast.makeText(this, "Beer added successfully!", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to add beer!", Toast.LENGTH_LONG).show()
                }
            } else {
                val selectionArs = arrayOf(id.toString())
                val mID = dbManager.update(values, "Id=?", selectionArs)

                if (mID > 0) {
                    Toast.makeText(this, "Beer added successfully!", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to add beer!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun imageViewToByte(image: ImageView): ByteArray {
        val bitmap = (image.getDrawable() as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE_GALLERY)
            } else {
                Toast.makeText(applicationContext, "You don't have permission to access file location!", Toast.LENGTH_SHORT).show()
            }
            return
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data

            try {
                val inputStream = contentResolver.openInputStream(uri!!)

                val bitmap = BitmapFactory.decodeStream(inputStream)
                imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, bitmap.width/8, bitmap.height/8, true))

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}