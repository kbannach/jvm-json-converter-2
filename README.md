# jvm-json-converter-2

Performs serialization of Object to JSON about 2 thimes quicker than Google's Gson.

## API

    QuickSon.SINGLETON.toJson(object)
    
## How to run

`mvn compile test`

### Feautures serialization of

- Objects with primitive types and strings
- Objects with collections
- Collections of primitives and objects
- Nested objects
- Handling nulls