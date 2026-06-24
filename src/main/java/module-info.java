module tech.sirwellington.alchemy.test {
    requires java.sql;
    requires org.junit.jupiter.api;
    requires org.slf4j;
    requires tech.sirwellington.alchemy.annotations;
    requires tech.sirwellington.alchemy.generator;

    exports tech.sirwellington.alchemy.test.junit;
    exports tech.sirwellington.alchemy.test.junit.generation;
}