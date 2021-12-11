CREATE TABLE IF NOT EXISTS "Suppliers" (
	"supplierBN"	INTEGER,
	"name"	TEXT NOT NULL,
	"bankAccount"	INTEGER NOT NULL,
	"termOfPayment"	TEXT NOT NULL,
	"needDelivery"	INTEGER NOT NULL,
	PRIMARY KEY("supplierBN")
);
CREATE TABLE IF NOT EXISTS "SuppliersTermsOfSupply" (
	"supplierBN"	INTEGER,
	"day"	TEXT,
	PRIMARY KEY("supplierBN","day")
);
CREATE TABLE IF NOT EXISTS "Orders" (
	"orderID"	INTEGER,
	"issueDate"	TEXT NOT NULL,
	"supplyDate"	TEXT CHECK("supplyDate" >= "issueDate"),
	"supplierBN"	INTEGER,
	"totalOrderPrice"	REAL NOT NULL CHECK("totalOrderPrice" >= 0),
	"isClosed"	INTEGER NOT NULL,
	PRIMARY KEY("orderID")
);
CREATE TABLE IF NOT EXISTS "Contacts" (
	"name"	TEXT,
	"supplierBN"	INTEGER,
	"phoneNumber"	TEXT NOT NULL,
	"email"	TEXT NOT NULL,
	FOREIGN KEY("supplierBN") REFERENCES "Suppliers"("supplierBN") ON DELETE CASCADE ON UPDATE CASCADE,
	PRIMARY KEY("name","supplierBN")
);
CREATE TABLE IF NOT EXISTS "SuppliersIncludedItems" (
	"supplierBN"	INTEGER,
	"catalogNumber"	INTEGER,
	"serialNumber"	INTEGER NOT NULL,
	"price"	REAL,
	FOREIGN KEY("supplierBN") REFERENCES "Suppliers"("supplierBN") ON DELETE CASCADE ON UPDATE CASCADE,
	PRIMARY KEY("supplierBN","catalogNumber")
);
CREATE TABLE IF NOT EXISTS "SuppliersOrdersDiscounts" (
	"supplierBN"	INTEGER,
	"amount"	INTEGER CHECK("amount" >= 1),
	"discount"	REAL CHECK("discount" >= 0),
	FOREIGN KEY("supplierBN") REFERENCES "Suppliers"("supplierBN") ON DELETE CASCADE ON UPDATE CASCADE,
	PRIMARY KEY("supplierBN","amount")
);
CREATE TABLE IF NOT EXISTS "SuppliersItemsDiscounts" (
	"supplierBN"	INTEGER,
	"catalogNumber"	INTEGER,
	"amount"	INTEGER CHECK("amount" >= 1),
	"discount"	REAL NOT NULL CHECK("discount" >= 0),
	FOREIGN KEY("supplierBN") REFERENCES "Suppliers"("supplierBN") ON DELETE CASCADE ON UPDATE CASCADE,
	PRIMARY KEY("supplierBN","catalogNumber","amount")
);
CREATE TABLE IF NOT EXISTS "OrderItems" (
	"orderID"	INTEGER,
	"catalogNumber"	INTEGER,
	"name"	TEXT NOT NULL,
	"amount"	INTEGER NOT NULL CHECK("amount" >= 1),
	"cost"	REAL NOT NULL CHECK("cost" > 0),
	"discountCost"	REAL NOT NULL CHECK("discountCost" > 0),
	"totalCost"	REAL NOT NULL CHECK("totalCost" > 0),
	FOREIGN KEY("orderID") REFERENCES "Orders"("orderID") ON DELETE CASCADE ON UPDATE CASCADE,
	PRIMARY KEY("orderID","catalogNumber")
);

