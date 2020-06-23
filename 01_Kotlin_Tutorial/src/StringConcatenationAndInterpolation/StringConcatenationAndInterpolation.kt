package StringConcatenationAndInterpolation

fun main() {
    val name: String = "Valentin"
    val age: Int = 20
    //Concatenation
    println("My name is " + name)
    println("My age is " + age)
    //String interpolation, kotlin replace variables with the values.String interpolation is used by prefixing the variable name with dollar symbol.
    print("My name is $name and my age is $age")
    //String interpolation by using expressions
    print("In the next 5 years i will have ${age+5}")
}