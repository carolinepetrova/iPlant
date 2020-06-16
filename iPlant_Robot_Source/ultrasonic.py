import RPi.GPIO as GPIO
import time
import signal
import sys
import paho.mqtt.client as paho

def on_connect(client, userdata, flags, rc):
    print("Connection returned result: "+connack_string(rc))

def on_publish(client, userdata, mid):
    print(client + " " + userdata + " " + mid)

broker="localhost"
port=1883
user="guest"
password="guest"
client1=paho.Client()
client1.on_connect = on_connect
client1.on_publish = on_publish
client1.username_pw_set(user, password=password)    #set username and password
client1.connect(broker,port)
client1.loop_start()



# use Raspberry Pi board pin numbers
GPIO.setmode(GPIO.BCM)

# set GPIO Pins
pinTrigger = 22
pinEcho = 27

def close(signal, frame):
	print("\nTurning off ultrasonic distance detection...\n")
	GPIO.cleanup()
	sys.exit(0)

signal.signal(signal.SIGINT, close)

# set GPIO input and output channels
GPIO.setup(pinTrigger, GPIO.OUT)
GPIO.setup(pinEcho, GPIO.IN)

while True:
	# set Trigger to HIGH
	GPIO.output(pinTrigger, True)
	# set Trigger after 0.01ms to LOW
	time.sleep(0.00001)
	GPIO.output(pinTrigger, False)

	startTime = time.time()
	stopTime = time.time()

	# save start time
	while 0 == GPIO.input(pinEcho):
		startTime = time.time()

	# save time of arrival
	while 1 == GPIO.input(pinEcho):
		stopTime = time.time()

	# time difference between start and arrival
	TimeElapsed = stopTime - startTime
	# multiply with the sonic speed (34300 cm/s)
	# and divide by 2, because there and back
	distance = (TimeElapsed * 34300) / 2
	#print ("Distance: %.1f cm" % distance)
	ret= client1.publish("testProximity",distance,2)
	time.sleep(0.1)