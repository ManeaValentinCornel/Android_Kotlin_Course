package ClassesAndObjects

fun main() {
    val bob = Player("Bobo")
    bob.show()
    val louse = Player("Louise", 10)
    louse.show()
    val kristof = Player("Kristof", 4, 8)
    kristof.show()
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    val radu = Player("Radu", 2, 5, 1000)
    radu.show()
    println(radu.wepon.name.toUpperCase())
    println(radu.wepon.damageInflictedL)

    val axe = Weapon("Axe", 12)
    radu.wepon = axe
    println(radu.wepon.name)
    println(radu.wepon.damageInflictedL)
    axe.damageInflictedL=20
    println(radu.wepon.damageInflictedL)






}