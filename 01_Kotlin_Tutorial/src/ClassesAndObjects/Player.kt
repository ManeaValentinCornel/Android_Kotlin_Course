package ClassesAndObjects

//The most common way of adding attributes is when we declare the class
class Player(val name: String,var level:Int=1,var lives:Int=3,var score:Int=0) {

    var wepon:Weapon=Weapon("Fist",1)

    fun show(){
        //Triple quotes a way to split the string over several lines
        println(""" 
            |name: $name
            |lives: $lives
            |level: $level
            |score: $score
            |wepons:${wepon.name}
            |namage: ${wepon.damageInflictedL}
        """.trimMargin())
    }
}
