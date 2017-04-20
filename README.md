[![Build Status](https://api.travis-ci.org/lkwg82/fdupes.java.svg?branch=master)](https://api.travis-ci.org/lkwg82/fdupes.java.svg?branch=master)

# fdupes
sort of java port of fdupe

original: https://github.com/adrianlopezroche/fdupes

# aim
- easier to use and faster!
- just playing with IO and system programming

# building

```bash
./mvnw clean verify
```

# running
```bash
java -jar target/fdupes*.jar <dir> [minimumFilesizeInMB [maximumFilesizeInMB]]
```
# alternatives
- original fdupe https://github.com/adrianlopezroche/fdupes
- another java implementation https://github.com/cbismuth/fdupes-java
