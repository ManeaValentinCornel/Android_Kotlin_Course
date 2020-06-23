package Encapsulation_PrivateProperties_Overloading

//The most common way of adding attributes is when we declare the class
class Player(val name: String, var level: Int = 1, var lives: Int = 3, var score: Int = 0) {

    var wepon: Weapon = Weapon("Fist", 1)
    //marking inventory as private
    //inventory is now encapsulated and is not available t any code outside the player class
    private var inventory = ArrayList<Loot>()

    //instead of accessing the inventory directly us a method to add the loot the inventory list
    fun pickLoot(item: Loot) {
        inventory.add(item)
        //code to save the inventory goes here
    }

    fun dropLoot(item: Loot): Boolean {
        if (inventory.contains(item)) {
            inventory.remove(item)
            return true;
        }
        return false;
    }

    //Overloading mechanism, creating another version of the function with different parameters and Kotlin call the one which matches the argument's types
    //Overload dropLoot, same method name but different parameters
    fun dropLoot(name: String){

       if(inventory.removeIf{x->x.name==name}){
           print("$name dropped")
       }
        else{
           print("$name not dropped")
       }

    }

    fun show() {
        if (lives > 0) {
            println("$name is alive")
        } else {
            println("$name is dead")
        }
    }

    //for eachloop
    fun showInventory() {
        println("$name's inventory is")
        for (item in inventory) {
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
