# Log masking using logback appender with PatternLayout

Use cases

1. masks the entire value for some fields
2. masks all except last 4 characters for some other fields

I came up with this pattern

 `(?<=(customerName)=).*?(?=(,|\))) | (?<=(contactNumber|customerIdentifier)=).*?(?=[\S\s]{0,4}(,|\)))`. This pattern consists of two parts mirroring the two use cases described above,

1. `(?<=(customerName)=).*?(?=(,|\)))` matches the entire values for customerName
2. `(?<=(contactNumber|customerIdentifier)=).*?(?=[\S\s]{0,4}(,|\)))` matches the values except last four characters for contactNumber and customerIdentifier

Specify the fields in `logback.xml`. `MaskingPatternLayout` will fill in the fields from `logback.xml` and compile the pattern. Then for each log, it will match the pattern on the log and replace all the matches with `***`. 

Notes
- No whitespace after the field name, before `=`
  - e.g. `contactNumber =99998888` -> `contactNumber =99998888`
  - how to add? `(customerName)=)` -> `(customerName)\s?=)` or `(customerName)=\s{0,5)` for up to 5 whitespaces
  - watch out for performance penalty if the upper limit is too large
- Leading and trailing whitespace are included in the matched result
  - e.g. `customerIdentifier=S1234567D<whitespace>` -> `customerIdentifier=***67D<whitespace>`
  - excluding them might impose some performance penalty
- `)` and `,` identifies where the value ends. Pattern matching will trip if the value contains any of these characters.
  - e.g. `customerName=Ah, Boon Choo` -> `customerName=***, Boon Choo`
  - how to add other identifiers? `(,|\))` -> `(,|\)|:|$)` adds `:` and end-of-line (`$`)
  
## Demo using vanilla Java

```java
String patternString = "(?<=(customerName)=).*?(?=(,|\\)))|(?<=(contactNumber|customerIdentifier)=).*?(?=[\\S\\s]{0,4}(,|\\)))";
Pattern pattern = Pattern.compile(patternString, Pattern.MULTILINE);

String message = "Customer(id=1, customerIdentifier=S1234567D, customerName=Ah Beng Choo, gender=Male, contactNumber=99998888)";
Matcher matcher = pattern.matcher(message);
System.out.println(matcher.replaceAll("***"));
// prints Customer(id=1, customerIdentifier=***567D, customerName=***, gender=Male, contactNumber=***8888)

message = "Customer(id=1, customerIdentifier=S1234567D , customerName=Ah, Beng Choo, gender=Male, contactNumber =99998888";
matcher = pattern.matcher(message);
System.out.println(matcher.replaceAll("***"));
// prints Customer(id=1, customerIdentifier=***67D , customerName=***, Beng Choo, gender=Male, contactNumber =99998888)
```

Reference
- http://www.regular-expressions.info/lookaround.html
- https://regexr.com/5bnh3