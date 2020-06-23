package VariablesAndTypes

//A variable is just a name that we give to an are of memory so that we can refer to the contents by name
fun main() {
    //Create a variables of type String in the memory are with the contents Manea, which is labeled by myNAme.
    //We told the computer the memory area can store String values
    //Declare variable
    var myName: String
    myName = "Manea"
    //
    var salaryBob: Int = 32
    var monthlySalary = 4 * salaryBob
    print(monthlySalary)

    val apples:Double=2.0/5
    val oranges:Int=5
    print("The fruit are ${apples/oranges}")

}