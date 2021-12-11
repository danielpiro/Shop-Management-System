# ADSS_Group_L
## the instruction on how to use is also specified inside the System
### general instructions:
```
- it's importent to separate more then one word objects by using "-" for example, a category name dairy products
  should be written as "dairy-products"
- it's the user responsibility to update the inventory after making a sale of an Item, the sytem will not
  remove the item unless it was an action made by the user.
- before making action on an Item you need to create the Item so it will be in the sytem. 
```
### system actions:

```
1. Add new item/s to the inventory:
   if you want to add a new item to the system you will need to specify the Item's Id, manufacturer's Name,minimum Quantity
   that was decided before declaring an Item to be in shortage, weight by grams and
   categories that item belongs to (two categories and the third one is the wight that is declared separately).
2. Delete Item from inventory:
   to delete an Item you will need to specify the Item's Id.
3. Update stock of an Item:
   to update amount of an Item you will need to specify the Item's Id, amount 
   (can be positive number to add or negative to remove) and the expired date of the item.
4. Update Supplement Date:
   to update next supplement Date of an Item you will need to specify the Item's Id and the date.
5. Create a report:
   you have the option the choose between three diffrent reports to make: Damage Report, Info Report and Shortage Report.
   - Info Report can be made based on specified categories (one ore more) and show details about Items from that category or 
     can make full report including every Item in the System.
   - Damage Report will show details about Items that is damaged or their expiry date has passed.
   - Shortage Report will show items that are missing in stock or are about to be out of stock based on the minimum
     quantity and next supplement date that was specified for each Item. 
6. add Sale:
   if you want to add a new Sale of an Item you will need to specify the Item's ID, the supplier's price, the price the 
   item was sold to costumer and the amount of Items that was sold.
7. Report a damaged Item:
   if you want to report a damaged Item you will need to specify the Item's ID and the amount of damaged Items that was found
8. EXIT:
   unfortunately this action will shut down the System :(
```
**and the most important thing, remember to enjoy!!!**
