package Encapsulation_PrivateProperties_Overloading
enum class LootType{
    POTION,RING,ARMOR

}
class Loot(val name:String,val type:LootType,val value:Double) {
    override fun toString(): String {
        return "Loot name:'$name', type=$type,and its value=$value"
    }
}