    package com.yeongzhiwei.demolog;

    import ch.qos.logback.classic.PatternLayout;
    import ch.qos.logback.classic.spi.ILoggingEvent;

    import java.util.regex.Matcher;
    import java.util.regex.Pattern;

    public class MaskingPatternLayout extends PatternLayout {

        private String fullMaskProperties;
        private String partialMaskProperties;
        private Pattern maskPattern;

        public String getFullMaskProperties() {
            return fullMaskProperties;
        }

        public void setFullMaskProperties(String fullMaskProperties) {
            this.fullMaskProperties = fullMaskProperties;
            compilePattern();

        }

        public String getPartialMaskProperties() {
            return partialMaskProperties;
        }

        public void setPartialMaskProperties(String partialMaskProperties) {
            this.partialMaskProperties = partialMaskProperties;
            compilePattern();
        }

        private void compilePattern() {
            final StringBuilder patternString = new StringBuilder();

            if (fullMaskProperties != null && !fullMaskProperties.isEmpty()) {
                patternString.append("(?<=(");
                patternString.append(fullMaskProperties);
                patternString.append(")=).*?(?=(,|\\)))");
            }

            if (partialMaskProperties != null && !partialMaskProperties.isEmpty()) {
                if (patternString.length() > 0) {
                    patternString.append("|");
                }
                patternString.append("(?<=(");
                patternString.append(partialMaskProperties);
                patternString.append(")=).*?(?=[\\S\\s]{0,4}(,|\\)))");
            }

            if (patternString.length() > 0) {
                this.maskPattern = Pattern.compile(patternString.toString(), Pattern.MULTILINE);
            }
        }

        @Override
        public String doLayout(ILoggingEvent event) {
            final String message = super.doLayout(event);

            if (maskPattern != null) {
                Matcher matcher = maskPattern.matcher(message);
                return matcher.replaceAll("***");
            }

            return message;
        }
    }
