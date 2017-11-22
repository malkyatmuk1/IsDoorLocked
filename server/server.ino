#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>
#include <SPI.h>
#include <EEPROM.h>
#include <vector>
#include<Hash.h>

using namespace std;

 struct person 
{
  char username[10];
  char pass_hash[20];
  char salt[12];
  char perm;
};

#define user_start  78
#define user_step  sizeof(person)
#define passAP_start  68
#define passAP_stop  77 
#define permission_start 120 

// declare the Variables
char* ssid="JohnAndWillow";
char* password="IamSuperProgrammer";
char passAp[10];
char* ssidAp="isdoor";

person list[10];
byte listofflags[10];
vector<String> commands;

vector<String> splitString(String line, char c);
void writePassAp(String pass);
void writeWifi(String passWifi,String ssidWifi);
char* salt_random();
void readPerson(uint index, person* p);
void writePerson(int start, person* p);
char ishavingPermission(String username1);
void printlist();
void printt(char* s,int n);

//create wifi server listen on a given port
WiFiServer server(3030);
//create Wifi client
WiFiClient client;


int button = 16;
String str,hsh;
bool buttonState;
// eeprom memory byte
int passwifi_start = 0, passwifi_stop = 31;
int ssidwifi_start = 32, ssidwidi_stop = 63;
int salt_start = 64, salt_stop = 67;

void setup() {
 Serial.begin(115200);
  //for(int i=0;i<10;i++) readPerson(i, &list[i]);
  EEPROM.begin(512);
  int p=76;
  int k=1;
  int i=0;
  int count=user_start;
  for(int i=0;i<10;i++)
  {
    readPerson(count,&list[i]);
    if(strlen(list[i].username)==0)listofflags[i]=0;
    else listofflags[i]=1;
    count+=user_step;
  }
  for(int i=0;i<10;i++)
  {
    Serial.println(listofflags[i]);
  }
  printlist();
  pinMode(button, INPUT);
  
  WiFi.softAP(ssidAp, passAp);
  if(strlen(ssid)!=0)
  {
    WiFi.begin(ssid, password);
  }
  Serial.println("");
  //Print status to Serial Monitor
  Serial.print("connected to: "); Serial.println(ssid);
  Serial.print("IP Address: "); Serial.println(WiFi.localIP());

  server.begin();
  Serial.print("tuk");
}

void loop()
{
  client = server.available();

  if (client)
  {
      
      Serial.println("connected to client");
      client.setTimeout(30000);
      str=client.readStringUntil('\n');
      commands=splitString(str,' ');
      //registrirane
      //v
      if(commands[0]=="signup")
      {
        bool flag=0,flag1=1;
        int j=0;
        person p;
        
        commands[1].toCharArray(p.username,sizeof(commands[1]));
        
        char salt[13];
        for(int i=0;i<12;i++){
        salt[i]=char(random(33,126));
        }
        salt[12]=0;
        //printt(salt,12);
       
        commands[2]+=salt;
        strncpy(p.salt,salt,12);
        sha1(commands[2]).toCharArray(p.pass_hash,20);
        bool haveUser=false;
        int ind,br=0;
        for(ind=0;ind<10;ind++) 
        {
          //if there is person, chech if the username is the same as the username that is read form the client 
          if( listofflags[ind]==1)
          {
            br++;
            if(!strncmp(p.username,list[ind].username,10)){haveUser=true;break;}
          }
        }
        //if this is the first user, set as admin
        if(br==0)p.perm='a';
        else p.perm='d';
        //if there is no user whit this username we signup in the first free space in the list of users and in the EEPROM
        if(!haveUser)
        {
         for(int i=0;i<10;i++)
         {
           if(listofflags[i]==0)
           {
            strncpy(list[i].username,p.username,10);
            strncpy(list[i].pass_hash,p.pass_hash,20);
            //printt(salt,12);
            strncpy(list[i].salt,salt,12);
            //printt(list[i].salt,12);
            list[i].perm=p.perm;
            writePerson(user_start+i*user_step,&p);
            listofflags[i]=1;
            break;
           }
         }
         printlist();
         EEPROM.commit();
         client.println("truesignup");
        }
        else client.println("errorsignup");
      }
      //vlizane
      //v
      else if(commands[0]=="signin")
      {
        char perm1;
        bool flag=0;
        person p;
        char passh[20];
        commands[1].toCharArray(p.username,10);
        String pass=commands[2];
        char salt[12];
                
        for(int i=0;i<10;i++)
       {
        
          //Serial.println(list[i].username);
          //Serial.println(p.username);

          
          //if the username form the client and the username in the list are equals we return the permission to the client
          if(!strncmp(list[i].username,p.username,10))
          {
            flag=1;
            //Serial.println(flag);
            
            strncpy(salt,list[i].salt,12);
            perm1=list[i].salt[12];
            salt[12]='\0';
            pass+=salt;      
            //we hash the password+salt
            sha1(pass).toCharArray(passh,20);
            Serial.println(passh);
            Serial.println(list[i].pass_hash);
            //check if the hashed pass form the client and the hashed password form the list are the same-> if is false the flag  become 0 
            if(strncmp(passh,list[i].pass_hash,20)) flag=0;
            Serial.println(flag);
            break;
          }
          else flag=0;
       }
          /*
          if(flag==1)
          {
           
            Serial.println("vuv flag=1");
            //printt(salt,12);
            Serial.println(salt);
            Serial.println(list[i].salt);
          
            
          }
           */
      if(flag==1) client.println(perm1);
      else client.println("errorsignin");
      }
      //v
      else  if(commands[0]=="setWifi")
      {
        int flag;
        char nameuser[10];
        commands[3].toCharArray(nameuser,10);
         /*  
        for(int i=0;i<10;i++)
       {
         if(!strncmp(list[i].username,nameuser,10) && list[i].perm=='a')
         {
            flag=1;
            break;
         }
        }
        */
        
        commands[1].toCharArray(ssid,32);
        commands[2].toCharArray(password,32);
        byte value;
        byte value1;
        WiFi.begin(ssid, password);
        writeWifi(commands[1],commands[2]); 
        if(flag==1) {client.println("true");}
        else client.println("false");
      }
       
       else if(commands[0]=="e")
       {
        for(int i=0;i<512;i++)
        {
          Serial.print(char(EEPROM.read(i)));
        }
       }
      //v  
      else if(commands[0]=="setAP")
      {
       writePassAp(commands[1],passAP_start);
      }
      else if(commands[0]=="list")
      {
        char nameuser[10];
        
        commands[1].toCharArray(nameuser,10);      
        String str;
        char perm1;
        bool flag=0;
        person p;
        char passh[20];
        commands[1].toCharArray(p.username,10);
        String pass=commands[2];
        char salt[12];
          /*       
        for(int i=0;i<10;i++)
       {        
            
           
            if(!strncmp(list[i].username,p.username,10))flag=1;
            else {flag=0;}
            Serial.println(list[i].perm);
           ;
          if(flag==1)
          {
            strncpy(salt,list[i].salt,12);
            //printt(salt,12);
            salt[12]='\0';
            pass+=salt;
            sha1(pass).toCharArray(passh,20);
            
            if(!strncmp(passh,list[i].pass_hash,20)){ flag=1;}
            else flag=0;
            break;
          }
         }
      
       */
       strncpy(salt,list[0].salt,12);
      //printt(salt,12);
       salt[12]='\0';
       pass+=salt;
        sha1(pass).toCharArray(passh,20); 
       //check if the user is admin
       if(!strncmp(passh,list[0].pass_hash,20)&&!strncmp(list[0].username,p.username,10)){ flag=1;}
       else flag=0;
       //if is admin make the list 
       if(flag==1)
       {
        for(int i=0;i<10;i++)
        {
          //if the flag is 1 in the list with flags and the user is not an admin then we print username and permission
         if(listofflags[i]==1 && list[i].perm!='a')
         {
            str=str+list[i].username;
           str+=' ';
           str+=list[i].perm;
           client.println(str);
           str="";
         }
        }
       }
       else client.println("you are not admin");
        client.println("stop");
      }
      //v
      else if(commands[0]=="setPermission")
      {
        char nameuser[10];
        
        commands[1].toCharArray(nameuser,10);      
        String str;
        bool flag=0;
        person p;
        char passh[20];
        commands[3].toCharArray(p.username,10);
        String pass=commands[4];
        char salt[12];
        strncpy(salt,list[0].salt,12);
      //printt(salt,12);
       salt[12]='\0';
       pass+=salt;
        sha1(pass).toCharArray(passh,20); 
        //setPermission name perm nameadmin passadmin
        char username1[10];
        commands[1].toCharArray(username1,10);
        String permission=commands[2];
        char perm1=permission[0];
        if(!strncmp(passh,list[0].pass_hash,20)&&!strncmp(list[0].username,p.username,10)){ flag=1;}
          else flag=0;
        for(int i=0;i<10;i++)
        {
          if(!strncmp(list[i].username,username1,10))
          {         
            if(flag==1)
            {
              list[i].perm=perm1;
              EEPROM.write(permission_start+i*user_step,perm1);
              EEPROM.commit();
              Serial.println(EEPROM.read(permission_start+i*user_step));
              client.println(perm1);
              break;
            }
          }
         
        }
      }
      //v
      else if(commands[0]=="del")
      {
        char username1[10];
        commands[1].toCharArray(username1,10);
        bool flag=0;
        String str;
        char perm1;
        person p;
        char passh[20];

        
        commands[2].toCharArray(p.username,10);
        Serial.println(commands[3]);
        String pass=commands[3];
        char salt[12];
        strncpy(salt,list[0].salt,12);
        //printt(salt,12);
         salt[12]='\0';
         pass+=salt;
         sha1(pass).toCharArray(passh,20); 
         //check if the user is admin
         
         if(!strncmp(passh,list[0].pass_hash,20)&&!strncmp(list[0].username,p.username,10)){ flag=1;}
         else flag=0;
               Serial.print(flag);
        for(int i=1;i<10;i++)
       {
           Serial.println(username1);
            Serial.println(list[i].username);
         if(!strncmp(list[i].username,username1,10)&& flag==1) 
         {
          int start_fordel=user_start+i*user_step;
          listofflags[i]=0;
          for(int j=0;j<user_step;j++)EEPROM.write(start_fordel+j,0);
          EEPROM.commit(); 
          int count=user_start;
          for(int i=0;i<10;i++)
          {
            readPerson(count, &list[i]);
            count+=user_step;
          }
          printlist();
          break;
        }
       }
      }
      
      //v
      else if (commands[0] == "take")
      {
        char perm1;
        bool flag=0;
        person p;
        char passh[20];
        commands[1].toCharArray(p.username,10);
        String pass=commands[2];
        char salt[12];
       for(int i=0;i<10;i++)
       {
        
          //Serial.println(list[i].username);
          //Serial.println(p.username);

          
          //if the username form the client and the username in the list are equals we return the permission to the client
          if(!strncmp(list[i].username,p.username,10))
          {
            flag=1;
            //Serial.println(flag);
            
            strncpy(salt,list[i].salt,12);
            salt[12]='\0';
            pass+=salt;      
            //we hash the password+salt
            sha1(pass).toCharArray(passh,20);
            Serial.println(passh);
            Serial.println(list[i].pass_hash);
            //check if the hashed pass form the client and the hashed password form the list are the same-> if is false the flag  become 0 
            if(strncmp(passh,list[i].pass_hash,20)) flag=0;
            perm1=list[i].perm;
            break;
          }
          else flag=0;
       }
         
        
         if(flag==1 && (perm1=='a' || perm1=='p'))
         {
          
       // read the state of the pushbutton value:
          buttonState = digitalRead(button);
          if (buttonState == HIGH)
          {
           client.println("open!");
          }
          else
          {
            client.println("close");
          }
         }
         else client.println(perm1);
        
      }
      else
      {
      client.println("error");
      }
    client.flush();
    client.stop();
  }
}
void writePerson(int start, person* p)
{
  for ( int i = 0; i < sizeof(person); i++) 
  {
    EEPROM.write(start+i, *((byte*)p+i));
    Serial.print(char(EEPROM.read(start + i)));
  }
  EEPROM.commit();
}
//v
void readPerson(int start, person* p)
{
    for ( int i = 0; i < sizeof(person); i++) *((byte*)p+i) = EEPROM.read(start + i);
  
}

//v
void writeWifi(String passWifi,String ssidWifi)
{  
  for(int i=0;i<passWifi.length();i++)
  {    
    EEPROM.write(i,passWifi[i]);
    //Serial.print(char(EEPROM.read(i))); 
  }
  for(int i=0;i<ssidWifi.length();i++)
  {    
    EEPROM.write(i+32,ssidWifi[i]);
   // Serial.print(char(EEPROM.read(i+32))); 
  }
    EEPROM.commit();
}
//v
char* salt_random()
{
  
  int random_number;
  static char salt[12];
  for(int i=0;i<12;i++){
  random_number=random(33,126);
  salt[i]=char(random_number);
  }
  return salt;
}
//v
void writePassAp(String pass,int start)
{
  int j=0;
   for(int i=start;i<pass.length()+start;i++)
   {
    
     //EEPROM.write(i,pass[j]);
     j++;
   }
}
//v
vector<String> splitString(String line, char c)
{
    vector<String> str;
    
    String curr_string = "";
    
    for(int i = 0; i < line.length(); i++)
    {
      if(line[i] == c)
      {

        str.push_back(curr_string);
        curr_string = "";
      }
      else curr_string+=line[i];
    }

    str.push_back(curr_string);
    return str;
}
char ishavingPermission(String username1)
{
 bool flag=0;
 char perm='d';
  
  for(int i=0;i<10;i++)
  {
    if(list[i].username[0]==username1[0])
    {
      for(int j=1;j<username1.length();j++)
      {
          if(list[i].username[j]==username1[j]){flag=1;perm=list[i].perm;}
          else {flag=0;perm='d';break;}
          
      }
    }
  }
  return perm;
}
void printlist()
{
  for(int i=0;i<10;i++)
  {
    if(list[i].username[0]!='\0'){
      Serial.print("User:");
      Serial.println(i);
      printt(list[i].username,10);
      printt(list[i].pass_hash,20);
      printt(list[i].salt,12);
      printt(&list[i].perm,1);
    }
  }
}
void printt(char* s,int n)
{
  for(int i=0;i<n;i++)
  {
    if(s[i]==0)break;
    Serial.print(s[i]);
  }
  Serial.println();
}


