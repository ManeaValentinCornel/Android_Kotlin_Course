package ForLoops

fun main(){
    //0 to 10 ending value is included in the range
    for(i in 0..10){
        println("i squared is ${i * i}")
    }

    //0 to 9, ending vlaue is not included in the range
    for(i in 0 until 10){
        println("i squared is ${i * i}")
    }

    //reverse order 10 to included 0
    for(i in 10 downTo 0){
    }
    //loops with steps 1 3 4 7 9. step has to be positive
    for(i in 1..10 step 2){
        print(i)
    }
    //challenge
    //Create a loop that print out all the numbers from 0 to 100 that are divisible by both 3 and 5
    for (i in 0..100 step 15){
        println(i)
    }
    

}

