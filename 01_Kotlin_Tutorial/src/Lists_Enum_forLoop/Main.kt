package Lists_Enum_forLoop
    fun main(){

        val player=Player("Bobo")
        val sword=Weapon("sword",10)
        player.wepon=sword
        val redPotion=Loot("Red Potion",LootType.POTION,7.50)
        player.inventory.add(redPotion)
        val armor=Loot("+3 Crest Armor",LootType.ARMOR,20.0)
        player.inventory.add(armor)
        player.inventory.add(Loot("+2 Ring of Protection",LootType.RING,40.25))


        print(player)
        player.show()
        player.showInventory()

    }
