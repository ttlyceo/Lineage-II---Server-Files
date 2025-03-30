You need to put the zeus folder into the de game/config folder of your server.

1.- Main Setting - You need to set this Values on zeus.properties.
	<a> USER -> The User of the Game Server Database.
	<b> HOST -> The Host of the GameServer, allways are 127.0.0.2
	<c> L2JLOGINSERVER_NAME -> The Login Server Database name.
	
2.- Annoucement Command's - Write this Command on annoucement's
	[VOTE]
	[RECALL] -> Work only if the gathering event is ON
	[RBEVENT]
	[EVENT]
	[SERVER_INFO]
	[HOME_CB]
	[COMMAND_INFO]
	[DONATION]
	[ACC_REGISTER]
	[CHANGE_PASSWORD]
	[CHANGE_EMAIL]
	[REMOVE_SECOND_PASS]
	[ACC_RECOVERY]
	[SELL_ACCOUNT]
	[SELL_CLAN]
	[DRESSME]
	[FIXME]
	[OLY_BUFFER]
	[LANGUAGE]
	
3.- HTML, XML, MULTISELL DATA
	bypass -h zeus_multisell [ID FILE]
	bypass -h zeus_exc_multisell [ID FILE]
	bypass -h zeus_buy [ID FILE]
	
4.- Dressme extra Commands
	You can set all the part to be use just for NOBLE, TOP PVP/PK or JUST HERO.
	- Just Noble:
		You need to put this Command: justnoble="true"
			Example:
				<set name="Moirai Leather Breastplate" ids="15610" cost="57,300000" justnoble="true" />
	- Just Top PvP/Pk:
		You need to put this Command: fortoppvp="true"
			Example:
				<set name="Moirai Leather Breastplate" ids="15610" cost="57,300000" fortoppvp="true" />
	- Just Heros:
		You need to put this Command: forhero="true"
			Example:
				<set name="Moirai Leather Breastplate" ids="15610" cost="57,300000" forhero="true" />
	
	- Just for One Clan (This allows only 1 clan use 1 or many Dressme items):
		You need to put this 3 new Commands: forexclusiveclan="true" forexclusiveclandays="30" forexclusiveclanprice="3470,1000;57,10000000"
			Example:
				<set name="Moirai Leather Breastplate" ids="15610" cost="57,300000" forexclusiveclan="true" forexclusiveclandays="30" forexclusiveclanprice="3470,1000;57,10000000" />
	- Just for One Player (This allows only 1 player use 1 or many Dressme items)
		You need to put this 3 new Commands: forexclusivechar="true" forexclusivechardays="30" forexclusivecharprice="23023,50" 
			Example:
				<set name="Moirai Leather Breastplate" ids="15610" cost="57,300000" forexclusivechar="true" forexclusivechardays="30" forexclusivecharprice="23023,50" />
	- You can Use Exlusive Clan and Char in the same item.
			Example:
				<set name="Moirai Leather Breastplate" ids="15610" cost="57,300000" forexclusiveclanminlevel="10" forexclusivecharnminlevel="50" forexclusiveclan="true" forexclusiveclandays="30" forexclusiveclanprice="23023,50" forexclusivechar="true" forexclusivechardays="30" forexclusivecharprice="23023,50" />
	- Use Hood in a Set.
			You can add a Hood, Helment in you Armor/suit Dressme. You need this Command to do it "deco". This Work "ONLY" if the player have empty the 2 hair slot.
					Example:
						<set name="Moirai Leather Breastplate Suit" ids="15610" cost="57,300000" deco="42000" />
					
5.- Server Plus Extra % for Every Adena Cost (Just Multisell .xml)
	****** Only Work if you have set PERCENTAGE_PRICE_ADDED in zeus_server_plus.properties bigger than 0 ****** 
	Using this command updateAdena="false" the Item not be affect by the Extra Adena Charge
		Example:
			<ingredient id="57" count="25000000" updateAdena="false" />
			
6.- Server Logo:
	1: Create a new logo with this size -> 256 x 64
	2: Save it by a number (Remenber this Id Number in PNG format
	3: Go to this page (https://tinypng.com/), upload the PNG file
	4: when it ready, copy that image into the config/zeus/img folder
	5: Go to ZeuS.properties and search SERVER_LOGO_ID, and put the Id number of the Image.
	
7.- Donation Web Page (You need have any hosting to use this option):
	1: Create a new Folder and Remenber the Name (For this Example we call it ZeuS)
	2: Go to the PHP_FILES folder and Copy those 3 files and Paste into the new Hosting Folder ZeuS
	3: Setup the Information Setting in the zeusemail.php file
	4: When you done, you need to put the same data in zeus.properties on the Donation Web Setting section
	
8.- Some features need NPC ID, that you need to create into the DB (custom_npc, custom_npc_ai)

9.- This link can open some features from any HTML windows.
	1: Need to put this into all action option "bypass ZeuS openCB _________"
	2: You need to replace _________ for any of this command
		- buffer
		- go_party_leader
		- flag_finder
		- teleport
		- shop
		- warehouse
		- augment
		- subclass_manager
		- class_transfer
		- drop_search
		- pvp_pk_log_system
		- symbol_market
		- bug_report
		- transformation_manager
		- remove_Attribute
		- manual_augment
		- manual_enchant
		- manual_enchant_element
		- raidboss_info
		- blacksmith
		- miscelanius
		- party_matching
		- auction_house
		- bid_house
		- dressme
		- classes_stadistic
		- donation_manager
		- gm_list
		- oly_buff_scheme
		- all_commands
		- class_statistic
	3: Example:
		<button value="Open Here to open Teleport" action="bypass ZeuS openCB teleport" width=284 height=18 back="L2UI_ct1.button_df" fore="L2UI_ct1.button_df"> 

10.- PvP Zones Types:
	- INDIVIDUAL
	- CLASSES
	- PARTY
	- FREE_FOR_ALL
	- CLAN
	- DROP_ZONE
	
11.- Teleport System:
	- Now you have two option to use the Teleport Function
		- Database: You need to Enable the "Use ZeuS Teleport (From BD)" from //zeus_config, Teleport
		- HTML Files: You need to Disable the "Use ZeuS Teleport (From BD)" from //zeus_config, Teleport
			- If you gonna use this Setting you need to create a Folder into the "data/html/teleporter/" called "ZeuS-GK".
			- You need to Create a Index.htm File.
			- All the Other HTML need to be in the Same Folder.
			- Bypass need to be this "bypass ZeuS tele [Commands and Action]"
			- Commands Bypass :
					- Go : "bypass ZeuS tele go x,y,x"
							- Need to replace the x,y,z for any place.
								Example Giran: "bypass ZeuS tele go 81911,148082,-3469"
					- HTML: "bypass ZeuS tele html [page name].htm"
							- Need to replace [page name] for the Name of the htm page
								Example: "bypass ZeuS tele html giran.htm"

12.- Buffer System:
	- Now you can add a Buff with reuse time using this setting reuse. You can added if you want for premium too.
		Example for all player:
			<set id="789" name="Spirit of Shilen" descript="Receives the will of Shilen" level="1" active="true" reuse="80" />
		Example just for premium:
			<set id="789" name="Spirit of Shilen" descript="Receives the will of Shilen" level="1" active="true" forpremium="false" reuse="80" />
			
			
13.- BuffStore:
	- You need to create a New Peace zone whith type="PeacebuffZone"
	- All class are allow to sell buff, but the buff than can sell are that you had in the CB Buffer Setting
	

