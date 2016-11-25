#! /bin/bash -x
# Component test for the orders service. 
# Very dumb - create an order and verify there's an order for this product id.
# Assumes a fresh environment

curl -s -X POST -H "Content-Type:application/json" -d '{  "productId" : "13579",  "amount" : "3", "totalSum" : "12.5" }' ${CODEFRESH_ENV_URL}/orders
curl -s ${CODEFRESH_ENV_URL}/orders | grep 13579
