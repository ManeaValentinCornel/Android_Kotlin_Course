package Inheritance_While_break_continue

//By default Kotlin classes cannot be extended
abstract class Enemy(val name: String, var hitPoints: Int, var lives: Int) {

    open fun takeDamage(damage: Int) {
        val remainingHitPoints = hitPoints - damage;
        if (remainingHitPoints > 0) {
            hitPoints = remainingHitPoints
            println("$name took $damage points of damage, and has $hitPoints left")
        } else {
            lives -= 1
            if (lives > 0) {
                println("$name lost a life")
            } else {
                println("No life left, Ugly Troll is dead")
            }
        }
    }

    override fun toString(): String {
        return "Name: $name, Hitpoints:$hitPoints, Lives: $lives"
    }

}