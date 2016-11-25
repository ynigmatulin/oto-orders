#! /bin/bash -x
curl -s -X POST -H "Content-Type:application/json" -d '{  "productId" : "13579",  "amount" : "3", "totalSum" : "12.5" }' ${CODEFRESH_ENV_URL}/orders
curl -s ${CODEFRESH_ENV_URL}/orders | grep 13579
