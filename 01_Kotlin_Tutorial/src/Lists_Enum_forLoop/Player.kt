package Lists_Enum_forLoop

//The most common way of adding attributes is when we declare the class
class Player(val name: String, var level: Int = 1, var lives: Int = 3, var score: Int = 0) {

    var wepon: Weapon = Weapon("Fist", 1)
    var inventory = ArrayList<Loot>()

    fun show(){
        if(lives>0){
            println("$name is alive")
        }
        else{
            println("$name is dead")
        }
    }
    //for eachloop
    fun showInventory(){
        println("$name's inventory is")
        for (item in inventory){
            println(item)
        }
    }


    override fun toString(): String {
        return """ 
            |name: $name
            |lives: $lives
            |level: $level
            |score: $score
            |${wepon}
        """.trimMargin()
    }



}
