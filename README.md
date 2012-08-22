MapLidator (Map-vaLidator)
==========================

MapLidator (Map-vaLidator) is a simple Java utility class for easy Map data validation. For example for validating maps resulting from JSON deserialization.

MapLidador is in a very premature status, so many work is pending.

The aim
-------

MapLidator necessity arised when in a Java REST-Json API we needed to validate contents of the Json received.

If we try to avoid Java-POJO approach, there are not many options to do validation of fields.

The idea is to have a very simple, easy to use, dummy-proof, self descriptive validation API for Map Objects resulting from deserialization of Json Strings or Streams. An example would be the result of deserialize a Json stream using [Jackson](http://jackson.codehaus.org/).

Usage Example
-------------
Lets suposse we read our Json from a file and want to validate it:

    InputStream is = String.class.getClassLoader().getResourceAsStream("json/invoice.json");
    ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
    mapper.configure(Feature.ALLOW_COMMENTS, true);
    Map<String, Object> data = mapper.readValue(is, Map.class);
    validateLoginData(data);

Using MapLidator the validateLoginData(Map<String, Object> data) method would look like:

    private void validateInvoiceData(Map<String, Object> data) {
      //invoice data must contain fields: customer, order and organization
      MapLidator.validate(data, "customer", OBJECT, NOT_NULL);
      MapLidator.validate(data, "organization.companyAddress", OBJECT, NOT_NULL);
      MapLidator.validate(data, "customer.firstName", STRING, NOT_NULL, STRING_NOT_EMPTY);
      MapLidator.validate(data, "order", ARRAY, NOT_NULL);
    }

How to use it
-------------
Is just one class inside a Maven project... Use it the way it pleases you. Any comments, improvements, etc will be very wellcome.

 