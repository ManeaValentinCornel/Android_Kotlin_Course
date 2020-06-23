package Decisions

fun main() {
    //***************************************IF ELSE conditional statements********************************************//
    val lives = 3
    //Evaluate the condition and assign the value to a boolean type variable.Boolean very useful to store the state of things while the progrma is running
    val gameOver = (lives < 1)
    //IF keyword followed by the condition to test The conditions evaluate to type called boolean
    if (gameOver) {
        println("Game Over")
    } else {
        println("Your still alive")
    }
    //***************************************ELSE IF + READ LINE SCANNER********************************************//
    println("How old are you ?")
    //readline takes input from the keyboard
    val age = readLine()!!.toInt()
    println("age is: $age")

    val message: String
    if (age < 18) {
        message = "You're too young to vote"
    } else if (age == 100) {
        message = "Congratulations"
    } else {
        message = "You can vote"
    }
    print(message)
    //*************************************LIFT ASSIGNMENT out of if**********************************************//
    //In case the block contains multiple line of code the last line the one which is assgned
    val label: String
    val date = 20
    label = if (date > 10) {
        "Date expired"
    } else {
        "Product valid"
    }
    //*************************************WHEN STATEMENT**********************************************//
    val badEvent = false
    val score = 3
    when {
        badEvent -> print("Game was abandoned")
        score >= 1 -> print("You win")
        score == 0 -> print("0")
        score < 0 -> print("You lose")
    }
    //*************************************WHEN STATEMENT with LIFT ASSIGNMENT**********************************************//
    val result: String
    result = when {
        badEvent -> "Game was abandoned"
        score > 1 -> "You win"
        score == 1 -> "You lose"
        else -> "Game is still playing"
    }
}

