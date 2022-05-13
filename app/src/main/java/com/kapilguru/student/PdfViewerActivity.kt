package com.kapilguru.student

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.github.barteksc.pdfviewer.PDFView
import kotlinx.android.synthetic.main.activity_pdf_viewer.*
import okio.IOException
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class PdfViewerActivity : BaseActivity() {

    // creating a variable
    // for PDF view.
    var pdfView: PDFView? = null

    // url of our PDF file.
    var pdfurl: String? = null

    lateinit var apiHelper :ApiHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)
        setActionbarBackListener(this, custom_action_bar, getString(R.string.view_syllabus))
        // initializing our pdf view.
        pdfView = idPDFView
        loadPdf()
    }

    private fun loadPdf() {
        pdfurl = intent.getStringExtra(PDF_PARAM)
        pdfurl?.let {
            val executor: ExecutorService = Executors.newSingleThreadExecutor()
            val handler = Handler(Looper.getMainLooper())
            executor.execute(Runnable {
                //Background work here
                var inputStream: InputStream? = null
                try {

                    val url = URL(pdfurl)
                    // below is the step where we are
                    // creating our connection.
                    val urlConnection: HttpURLConnection = url.openConnection() as HttpsURLConnection
                    if (urlConnection.responseCode == 200) {
                        inputStream = BufferedInputStream(urlConnection.inputStream)
                    }
                    handler.post(Runnable {
                        pdfView?.fromStream(inputStream)?.load()
                    })
                } catch (ex: IOException) {

                }
            })
        }

    }

}