import paho.mqtt.client as paho
import json
from crontab import CronTab
import datetime
import threading
import RPi.GPIO as GPIO
import time

is_too_close = False
turn = "none"
topics = ["testProximity", "objectDetection"]
# This is the Subscriber

def on_connect(client, userdata, flags, rc):
  print("Connected with result code "+str(rc))
  for topic in topics:
    client.subscribe(topic, qos=2)

def on_message(client, userdata, msg):
    global is_too_close
    global turn
    if msg.topic == "testProximity":
        distance = float(msg.payload.decode('utf-8'))
        if distance < 15.0:
            is_too_close = True
        else:
            is_too_close = False
    elif msg.topic == "objectDetection":
        obj_detection = json.loads(msg.payload.decode('utf-8'))
        if obj_detection["object"] == "plant" or obj_detection["object"] == "pottedplant" or obj_detection["object"] == "person":
            coord1 = int(obj_detection["coords"][0])
            coord2 = int(obj_detection["coords"][1])
            center = (coord1 + coord2) / 2
            print(center)
            if center < 250:
                turn = "left"
            elif center > 250:
                turn = "right"
            else: 
                turn = "none"

    
broker="localhost"
port=1883
user="guest"
password="guest"
client=paho.Client("scheduler")
client.on_connect = on_connect
client.on_message = on_message
client.username_pw_set(user, password=password)


GPIO.setmode(GPIO.BCM)

fwdleft = 24
fwdright = 25

revleft = 23
revright = 27

motors = [fwdleft,fwdright,revleft,revright]

for item in motors:
	GPIO.setup(item, GPIO.OUT)

def forward(i):
	GPIO.output(fwdright, True)
	GPIO.output(fwdleft, True)

def right(i):
	GPIO.output(revright, True)
	GPIO.output(fwdleft, True)
	time.sleep(i)
	GPIO.output(revright, False)
	GPIO.output(fwdleft, False)

def left(i):
	GPIO.output(fwdright, True)
	GPIO.output(revleft, True)
	time.sleep(i)
	GPIO.output(fwdright, False)
	GPIO.output(revleft, False)

def reverse(i):
	GPIO.output(revleft, True)
	GPIO.output(revright, True)
	time.sleep(i)
	GPIO.output(revleft, False)
	GPIO.output(revright, False)

def subscribing():
    print("started thread")
    client.connect(broker,port)
    while(True):
        client.loop()

if __name__ == "__main__":
    mqtt_subscriber=threading.Thread(target=subscribing)
    # mqtt_subscriber.setDaemon(True)
    mqtt_subscriber.start()
    try:
        print("R E A D Y")
        while(1):
            #print(is_too_close)
            time.sleep(0.4)
            if is_too_close:
                time.sleep(1)
                print("turning left")
                left(0.5)
                is_too_close = False
            else:
                if turn == "none":
                    forward(50)
                    print("going forward")
                elif turn == "left":
                    left(0.1)
                    print("turning left")
                    turn = "none"
                elif turn == "right":
                    print("turning right")
                    right(0.1)
                    turn = "none"

    except KeyboardInterrupt:
        print("E X I T")
        GPIO.cleanup()
        mqtt_subscriber.terminate()
