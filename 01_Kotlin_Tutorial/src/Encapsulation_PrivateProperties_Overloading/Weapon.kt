package Encapsulation_PrivateProperties_Overloading

class Weapon(val name: String, var damageInflicted: Int) {

    override fun toString(): String {
        return "Weapon used name='$name',and damageInflicted=$damageInflicted"
    }
}