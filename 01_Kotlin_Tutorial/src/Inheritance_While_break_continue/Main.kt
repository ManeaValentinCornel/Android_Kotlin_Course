package Inheritance_While_break_continue

fun main() {
    val uglyTroll = Troll("Ugly Troll")
    println(uglyTroll)
    uglyTroll.takeDamage(10)

    val vlad = Vampire("Vlad")
    println(vlad)
    vlad.takeDamage(8)

    val dracula = VampireKing("Dracula")
    print(dracula)
    while (dracula.lives > 0) {
        if (dracula.dodges()) continue
        if (dracula.runAway()) {
            println("Dracula ran away")
            break

        } else {
            dracula.takeDamage(12)
        }
    }


}
