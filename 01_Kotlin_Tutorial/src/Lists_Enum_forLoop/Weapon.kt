package Lists_Enum_forLoop

class Weapon(val name: String, var damageInflicted: Int) {

    override fun toString(): String {
        return "Weapon used name='$name',and damageInflicted=$damageInflicted"
    }
}