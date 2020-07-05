package com.vcmanea.flickerapp
import android.util.Log
import java.io.IOException
import java.io.ObjectStreamException
import java.io.Serializable
//REASON OF IMPLEMENTING THE METHODS:writeObject,readObject,readObjectNoData
//When we provide thse three function, the JVM no longer has toi try to work out what goes where when serializing or de-serializing the data
//-->and that makes the process far more efficient
data class Photo(var title: String, var author: String, var authorId: String, var tags: String, var imageUrlSmall: String, var imgUrlBig: String) :
    Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }

    @Throws(IOException::class)
    private fun writeObject(out: java.io.ObjectOutputStream) {
        Log.d("Photo", "writeObject called")
        out.writeUTF(title)
        out.writeUTF(author)
        out.writeUTF(authorId)
        out.writeUTF(tags)
        out.writeUTF(imageUrlSmall)
        out.writeUTF(imgUrlBig)
    }

    @Throws(IOException::class,ClassNotFoundException::class)
    private fun readObject(inStream: java.io.ObjectInputStream){
        Log.d("Photo", "readObject called")
        title=inStream.readUTF()
        author=inStream.readUTF()
        authorId=inStream.readUTF()
        tags=inStream.readUTF()
        imageUrlSmall=inStream.readUTF()
        imgUrlBig=inStream.readUTF()
    }

    @Throws(ObjectStreamException::class)
    private fun readObjectNoData(){
        Log.d("Photo", "readObjectNoData called")
    }

}