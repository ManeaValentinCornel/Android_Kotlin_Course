package Encapsulation_PrivateProperties_Overloading

fun main() {

    val player = Player("Bobo")
    val sword = Weapon("sword", 10)
    player.wepon = sword
    val redPotion = Loot("Red Potion", LootType.POTION, 7.50)
    val bluePotion = Loot("Blue Potion", LootType.POTION, 10.00)
    player.pickLoot(redPotion)
    val armor = Loot("+3 Crest Armor", LootType.ARMOR, 20.0)
    player.pickLoot(armor)
    player.pickLoot(Loot("+2 Ring of Protection", LootType.RING, 40.25))

    print(player)
    player.show()
    player.showInventory()
    if (player.dropLoot(bluePotion)) {
        println("Inventory after the drop")
        player.showInventory()
    } else {
        println("you don't have a ${redPotion}")
    }
    //Overloading
    player.dropLoot("Red Potion")



}
