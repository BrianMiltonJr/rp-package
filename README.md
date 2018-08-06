# rp-package
A suite of rp plugins

I've decided I am going to release this to the community while it is in a very beginning phase and take input on 
what I should do. What I want to create in the end is a completely modular rp experience.

Current Features:
  * First & Last Names with genders
  * A chat system that goes by distance with the ability to whisper, talk or yell
  * A Out of Character chat system that can be toggle to talk into by using /ooc or using /ooc on | /ooc off to display ooc chat
  * Full JSON support
  * Karma System - Inspired by TTT to allow game-masters to penalize failRp and rules (Currently implemented, but barebones)
  * Commenting code for contributors (Currently up to date with V0.0.2)
  * more debug calls

What Im Working On:
  * Merging project-elrond into this plugin as a stat system

Planned Features:
  * Government or tribal implementation of a grouping system
  * MySQL or JSON support (JSON fully supported)
  * And much more

Permissions:

Rp Core:
  * rp.player - An admin command. Allows to search player's info in the database.

Rp Chat:
  * rp.whisper - Allows the user to whisper.
  * rp.talk - Allows the user to talk.
  * rp.yell - Allows the user to yell.
  * rp.color - Allows the user to color their own name.
  * rp.ooc

Rp Karma (Still Under heavy development, Will be released with V0.0.2):
  *rp.negate - allows an admin to negate karma from a player that is performing badly on the server.
  *rp.karma - allows an admin to check a player's karma file

Commands(first & last refer to names):

Rp Core:
  * /player - Usage: /player first last | Looks up player details.

Rp Chat:
  * /whisper or /w - Sets your chat distance to whisper
  * /talk or /t - Sets your chat distance to talk
  * /yell or /y - Sets your chat distance to yell
  * /color - Usage: /color color or /color name color | Colors your name
  * /ooc - Usage: /ooc switches between talking in ooc or in game chat. /ooc [on/off or 1/0] toggles ooc chat displaying

Rp Karma((Still Under heavy development, Will be released with V0.0.2)):
  * /negate - Usage: /negate first last offense description | Registers an offense on the offender's karma negating its total.
  * /karma - Usage: /karma first last | Prints player's karma file
  
