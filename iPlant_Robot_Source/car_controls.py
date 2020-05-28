import paho.mqtt.client as paho
import json
from crontab import CronTab
import datetime
import threading
import RPi.GPIO as GPIO
import time

is_too_close = False

# This is the Subscriber

def on_connect(client, userdata, flags, rc):
  print("Connected with result code "+str(rc))
  client.subscribe("testProximity", qos=2)

def on_message(client, userdata, msg):
    global is_too_close
    distance = float(msg.payload.decode('utf-8'))
    if distance < 15.0:
        print("I'm here")
        is_too_close = True
    else:
        is_too_close = False

    
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
	time.sleep(2)
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
    something = float("12.656656")
    try:
        print("R E A D Y")
        print(is_too_close)
        while(1):
            print(is_too_close)
            time.sleep(0.1)
            if is_too_close:
                print("turning")
                left(50)
                is_too_close = False
            else:
                print("going forward")
                forward(50)
    except KeyboardInterrupt:
        print("E X I T")
        GPIO.cleanup()
        mqtt_subscriber.terminate()
