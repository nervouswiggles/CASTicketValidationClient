# CASTicketValidationClient
The client used to initially receive a service ticket (ST=xxxx) to then retrieve the expected CAS response to the application (service) along with reviewing the attributes

# Steps to view the serviceResponse from CAS
1. Create a maven project by using the pom.xml file
2. Download the .java file and place this into your IDE
3. Change the 5 variables to match your environment
4. When you run the application, it should then present a ST-ticket
5. You then use this ticket to run the URL below. Remember to change the 2 URLs as well as replacing the ticket at the end. Make sure you do this no fewer than a few seconds after receving the ST ticket 
https://CASSERVER/sso/serviceValidate?service=https://APPLICATIONSERVER/WHATEVER/WHATEVER&ticket=ST-271-K3v21d6pvqTmp1tCR3gi-sso
