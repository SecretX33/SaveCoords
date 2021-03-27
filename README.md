# SaveCoords
 Simple minecraft plugin that saves player current pos in a .json file, with a name and a description. Created as requested by ItzMarni.

 I have used for the first time Guava's Multimap and serialization/deserialization of it, it was a good learning journey. Among the features, there are tabcompletion and all the safety checks you could ask for.
 
## Commands

* addcoords \<name> \<desc> 

Used to add your current location as coordinates with typed name and description, which gets saved to the file "coordinates.json". *Aliases:  ac, addcoord, addcoordinate, addcoordinates*

* removecoords \<name>

Used to remove any added coordinates. *Aliases: rc, removecoordinates, dc, delcoord, delcoords*