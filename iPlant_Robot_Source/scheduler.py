import paho.mqtt.client as mqtt
import json
from crontab import CronTab

# This is the Subscriber

def on_connect(client, userdata, flags, rc):
  print("Connected with result code "+str(rc))
  client.subscribe("scheduler")

def on_message(client, userdata, msg):
    printf(msg)
    payload_to_json = json.loads(msg.payload.decode())
    date_time_obj = datetime.datetime.strptime(payload_to_json['date'], '%d-%m-%Y %H:%M:%S')
    printf(date_time_obj.minute)
    #cron = CronTab(tab="""* * * * * command""")


    
client = mqtt.Client()
client.username_pw_set("guest", "guest")
client.connect("localhost",1883,60)

client.on_connect = on_connect
client.on_message = on_message

# and listen to server
run = True
while run:
    client.loop()


