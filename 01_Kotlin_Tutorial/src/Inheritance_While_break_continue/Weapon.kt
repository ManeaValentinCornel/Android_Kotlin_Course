package Inheritance_While_break_continue

class Weapon(val name: String, var damageInflicted: Int) {

    override fun toString(): String {
        return "Weapon used name='$name',and damageInflicted=$damageInflicted"
    }
}