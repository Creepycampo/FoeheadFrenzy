package com.johnmuindi.foreheadfrenzykenya.data

data class Category (
    val id :String,
    val name: String,
    val emoji: String,
    val  words :List <String>

)
 object WordBank {
     val categories = listOf(
         Category(
             id = "cities",
             name = "Kenyan Cities & Places",
             emoji = "",
             words = listOf(
                 "Nairobi", "Nanyuki", "Kisumu", "Eldoret", "Mombasa",
                 "Nakuru", "Thika", "Kitale", "Naivasha", "Machakos",
                 "Kericho", "Malindi", "Garissa", "Kakamega", "Nyeri",
                 "Meru", "Lamu", "Voi", "Athi River", "Ruiru",
                 "Karen", "Westlands", "Kibera", "Eastleigh", "CBD",
                 "Mlolongo", "Rongai", "Kawangware", "Mathare", "Buruburu"
             )
         ),
         Category(
             id = "celebrities",
             name = "Celebrities & Entertainers",
             emoji = "🌟",
             words = listOf(
                 "Eric Omondi", "Azziad Nasenya", "Nyashinski", "Khaligraph Jones",
                 "Willy Paul", "Akothee", "Diana Marua", "Bahati",
                 "Jalang'o", "Otile Brown", "Nadia Mukami", "Sauti Sol",
                 "Trio Mio", "Femi One", "Wahu", "Nameless", "Size 8",
                 "DNG", "Vera Sidika", "Karen Nyamu", "Terence Creative",
                 "Crazy Kennar", "MC Jessy", "Comedian Pitson", "Njugush",
                 "Wakavinye", "Abel Mutua"
             )
         ),
         Category(
             id = "animals",
             name = "Animals",
             emoji = "🦁",
             words = listOf(
                 "Lion", "Zebra", "Hyena", "Elephant", "Giraffe",
                 "Buffalo", "Cheetah", "Hippo", "Wildebeest", "Ostrich",
                 "Warthog", "Crocodile", "Leopard", "Rhino", "Baboon",
                 "Vulture", "Flamingo", "Mongoose", "Antelope", "Impala"
             )
         ),
         Category(
             id = "slang",
             name = "Kenyan Slang & Sheng",
             emoji = "🗣️",
             words = listOf(
                 "Mbogi", "Kanjo", "Chapo", "Mjengo", "Matatu",
                 "Sonko", "Mathe", "Buda", "Mtaa", "Fagia",
                 "Niko Sawa", "Vipi", "Msee", "Dame", "Kuna noma",
                 "Poa", "Sawasawa", "Manze", "Cheza poa", "Mbao",
                 "Kuna shida", "Form", "Bamba", "Soko", "Hustle",
                 "Mwarie", "Kufala", "Tuko Sawa", "Mzigo", "Shamba"
             )
         ),
         Category(
             id = "football",
             name = "Football",
             emoji = "⚽",
             words = listOf(
                 "Gor Mahia", "AFC Leopards", "Harambee Stars",
                 "Victor Wanyama", "Michael Olunga", "McDonald Mariga",
                 "Dennis Oliech", "FKF", "Kasarani Stadium", "Sofapaka",
                 "Tusker FC", "Ulinzi Stars"
             )
         ),
         Category(
             id = "food",
             name = "Kenyan Foods",
             emoji = "🍲",
             words = listOf(
                 "Ugali", "Sukuma Wiki", "Nyama Choma", "Chapati",
                 "Mukimo", "Githeri", "Mursik", "Mandazi", "Pilau",
                 "Mutura", "Omena", "Kachumbari", "Mokimo", "Matumbo",
                 "Samosa", "Bhajia", "Smokie Pasua", "Viazi Karai"
             )
         )
     )
     fun getCategoryById (id: String): Category? =
         categories.find { it.id == id }
 }