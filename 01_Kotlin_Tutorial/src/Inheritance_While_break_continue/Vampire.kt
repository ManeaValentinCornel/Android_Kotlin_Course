package Inheritance_While_break_continue

open class Vampire(name:String):Enemy(name,20,3) {

    override fun takeDamage(damage: Int) {
     super.takeDamage(damage/2)
    }

}