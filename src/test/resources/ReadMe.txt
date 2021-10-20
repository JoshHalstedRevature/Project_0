This document is written in order to provide the user instructions on how to utilize the Project0ScalaCode.scala file, 

MongoDB Commands

Calculate the mean lethargy gain per collision.

db.Thermal.aggregate(
[
    {
            $addfields :
            {
                "Alpha": {$divide : [{$add: ["$Molar Mass",1]}, {$subtract: ["$Molar Mass",1]}]}
            }
    }
])

