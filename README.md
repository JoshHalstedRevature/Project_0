This document is written in order to provide the user instructions on how to utilize the Project0ScalaCode.scala file.

First things first: save the JSON file into the src/test/resources folder. 

Then, compile and run the code in sbt. Type in the name of the file (with the ".json" extension) when prompted. If the file is both a .json file and can be found, the command prompt will then request you to provide a name for the collection. 

If there are "error" messages afterwards, it implies that the collection name is already taken. Recompile and rerun the Scala file, and provide a different collection name.




*******************************MongoDB Commands**********************************************


$ These commands rely on the "Thermal" collection. This is the name I provided when prompted by the SBT terminal.

CREATE:

db.Thermal.insertOne(
   { "Element Name": "Uranium", "Molar Mass": 235, "Atomic Weight": 235.0439299, "Half Life": "703800000.0 years", "Decay Mode": "Alpha Emission", "Absorption Micro CS": 7.68, "Scattering Micro CS": 8.3, "Total Micro CS": 16.0, "Mass Density": 19.1}
)



READ:

db.Thermal.find({"Element Name": "Hydrogen"
db.Thermal.find().pretty()
db.Thermal.aggregate(
   [
     { $sort : { "Mass Density" : -1 } }
   ]
).pretty()

db.Thermal.find("Element Name": "Hydrogen")
db.Thermal.find({"Decay Mode": "Proton Emission"})
db.Thermal.find( { $or: [ { "Decay Mode": "Proton Emission"}, { "Decay Mode": "Neutron Emission"} ] } )

UPDATE:

db.Thermal.updateOne(
   { "Element Name": "Uranium" },
   {
     $set: { "Absorption Micro CS": 90.0},
   }
)


DELETE:

db.Thermal.deleteOne({"Element Name": "Uranium"})
db.Thermal.deleteMany({"Half Life": "Unstable"})

AGGREGATE


**********

db.Thermal.aggregate([
    {	$addFields:
        {
            "alpha": {$multiply: [{$divide: [{$subtract: ["$Molar Mass", 1]}, 0.6]}, {$divide: [{$subtract: ["$Molar Mass", 0.5]}, {$add: ["$Molar Mass", 1]}]}]}
        }
    },
    {   $addFields:
        {
            "# Collisions to Thermalize": {$divide: [{$ln: {$divide: [2000000.0, 10.0]}}, "$Mass Density"]}
        }
    },
    {	$addFields:
        {
            "Moderating Power": {$multiply: [0.6, "$Scattering Micro CS"]}
        }
    },
    {	$addFields:
        {
            "Moderating Ratio": {$divide: ["$Moderating Power", "$Absorption Micro CS"]}
        }
    },
    {   $match: { "Half Life": "Stable" } },
    {   $project: {"Element Name": 1, "Molar Mass": 1, "Mass Density": 1, "Moderating Ratio": 1}},
    {   $sort: {"Moderating Ratio": -1}}
    ]
    ).pretty()


************

db.Thermal.aggregate([
{	$addFields:
    {
        "Number Density": {$divide: [{$multiply: [0.6022, "$Mass Density"]}, "$Atomic Weight"]}
    }
},
{	$addFields:
    {
        "Absorption Macro CS": {$multiply: ["$Number Density", "$Absorption Micro CS"]}
    }
},
{	$addFields:
    {
        "Absorption Mean Free Path (cm)": {$divide: [1, "$Absorption Macro CS"]}
    }
},
{	$addFields:
    {
        "Scattering Macro CS": {$multiply: ["$Number Density", "$Scattering Micro CS"]}
    }
},
{	$addFields:
    {
        "Scattering Mean Free Path (cm)": {$divide: [1, "$Scattering Macro CS"]}
    }
},
    {   $match: { "Half Life": "Stable" } },
    {   $project: {"Element Name": 1, "Molar Mass": 1, "Mass Density": 1, "Absorption Mean Free Path (cm)": 1, "Scattering Mean Free Path (cm)": 1}},
    {   $sort: {"Moderating Ratio": -1}}
    ]
    ).pretty()
