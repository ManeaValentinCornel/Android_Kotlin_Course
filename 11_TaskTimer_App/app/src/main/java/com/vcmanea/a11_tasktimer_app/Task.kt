package com.vcmanea.a11_tasktimer_app

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
//The reason why id is ns a var? when we saving a task we don;t know which the id is until it is saved to the database. We get the id only after we recorded the record
//data
//all the free functions coming with the data class, work just for the variable which are declared into the primary constructor
//in our case when we insert a Record into the database it won;t have an id. But when will provide a query the elemnts will have an id.
//-> just to avoid this behaviour it would be better to move the id variable and declare it within the class, so in case we have to create we won't get any errors
//So no one the equal variable won't be include into the equals tests, and will not be included for  other auto-generated functions
//If you don't use Data class functionality, you should consider to not use it at all. Because android has a limit of methods which ca be contained into an app.
//Less overheads in terms of memory -> Each DataClass you create may increase the method count by 18 -> Useful but only when you use what they provide
//val id : Long=0
@Parcelize
data class Task(val name: String, val description: String, val sortOrder: Int,var id:Long=0L) : Parcelable{
}
