import paho.mqtt.client as paho
import json
from crontab import CronTab
import datetime
import argparse

ap = argparse.ArgumentParser()
ap.add_argument("-u", "--user", required=True,
	help="User which will be scheduled the cronjob to")
ap.add_argument("-p", "--program", required=True,
	help="Program which will be scheduled to run")
args = vars(ap.parse_args())

# This is the Subscriber

def on_connect(client, userdata, flags, rc):
  print("Connected with result code "+str(rc))
  client.subscribe("scheduler", qos=2)

def on_message(client, userdata, msg):
    print(msg.payload.decode('utf-8'))
    payload_to_json = json.loads(msg.payload.decode('utf-8'))
    print(payload_to_json)
    print(payload_to_json["date"])
    date_time_obj = datetime.datetime.strptime(payload_to_json["date"].replace("T", " "), '%Y-%m-%d %H:%M')
    print(date_time_obj.minute)
    cron = CronTab(tab="""{} {} {} {} * {} -q {}"""
    .format(date_time_obj.minute,date_time_obj.hour, 
            date_time_obj.day, date_time_obj.month, args["program"], ",".join(payload_to_json["plantIDs"])))
    print(cron)
    cron.write(user=args["user"])

    
broker="192.168.1.109"
port=1883
user="guest"
password="guest"
client=paho.Client()
client.on_connect = on_connect
client.on_message = on_message
client.connect(broker,port)

client.loop_forever()