# USAGE
# python3 object_detection.py --prototxt MobileNetSSD_deploy.prototxt.txt --model MobileNetSSD_deploy.caffemodel &
# import the necessary packages
from imutils.video import VideoStream
from pyzbar import pyzbar
import argparse
import datetime
import imutils
import time
import cv2
import paho.mqtt.client as paho
import threading
import json
import numpy as np
from datetime import datetime
import base64


ap = argparse.ArgumentParser()
ap.add_argument("-p", "--prototxt", required=True,
	help="path to Caffe 'deploy' prototxt file")
ap.add_argument("-m", "--model", required=True,
	help="path to Caffe pre-trained model")
ap.add_argument("-c", "--confidence", type=float, default=0.2,
	help="minimum probability to filter weak detections")
args = vars(ap.parse_args())

# initialize the list of class labels MobileNet SSD was trained to
# detect, then generate a set of bounding box colors for each class
CLASSES = ["background", "aeroplane", "bicycle", "bird", "boat",
	"bottle", "bus", "car", "cat", "chair", "cow", "diningtable",
	"dog", "horse", "motorbike", "person", "pottedplant", "sheep",
	"sofa", "train", "tvmonitor", "hand"]
COLORS = np.random.uniform(0, 255, size=(len(CLASSES), 3))

# load our serialized model from disk
print("[INFO] loading model...")
net = cv2.dnn.readNetFromCaffe(args["prototxt"], args["model"])
# initialize the video stream, allow the cammera sensor to warmup,
# and initialize the FPS counter

def on_connect(client, userdata, flags, rc):
    print("Connection returned result: "+connack_string(rc))

def on_publish(client, userdata, mid):
    print(client + " " + userdata + " " + mid)

# construct the argument parser and parse the arguments

# initialize the video stream and allow the camera sensor to warm up
print("[INFO] starting video stream...")
# vs = VideoStream(src=0).start()
vs = VideoStream(usePiCamera=True).start()
time.sleep(2.0)
# open the output CSV file for writing and initialize the set of
# barcodes found thus far
found = set()

broker="localhost"
port=1883
user="guest"
password="guest"
client1=paho.Client("client1")
client1.on_connect = on_connect
client1.on_publish = on_publish
client1.username_pw_set(user, password=password)
client1.connect(broker,port)
client1.loop_start()


def barcode_scanner():
    # loop over the frames from the video stream
    while True:
        # grab the frame from the threaded video stream and resize it to
        # have a maximum width of 400 pixels
        frame = vs.read()
        name = datetime.now()
        name2= name.strftime("%d-%m-%Y_%H:%M:%S")
        cv2.imwrite("{}.jpg".format(name2), frame) # save frame as JPEG fil
        f=open("{}.jpg".format(name2), "rb") #3.7kiB in same folder
        #print("i am here 2")
        frame = imutils.resize(frame, width=400)

        # find the barcodes in the frame and decode each of the barcodes
        barcodes = pyzbar.decode(frame)
        #print("i am here 3")

        # loop over the detected barcodes
        for barcode in barcodes:
            # extract the bounding box location of the barcode and draw
            # the bounding box surrounding the barcode on the image
            (x, y, w, h) = barcode.rect
            cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 0, 255), 2)

            # the barcode data is a bytes object so if we want to draw it
            # on our output image we need to convert it to a string first
            barcodeData = barcode.data.decode("utf-8")
            barcodeType = barcode.type
            ret= client1.publish("barcodeReader",barcodeData,2)

            # draw the barcode data and barcode type on the image
            text = "{} ({})".format(barcodeData, barcodeType)
            cv2.putText(frame, text, (x, y - 10),
                cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 2)

            # if the barcode text is currently not in our CSV file, write
            # the timestamp + barcode to disk and update the set

        # show the output frame
        #cv2.imshow("Barcode Scanner", frame)
        key = cv2.waitKey(1) & 0xFF
        # if the `q` key was pressed, break from the loop
        if key == ord("q"):
            break

def object_detection():
	while True:
		#fps = FPS().start()
		# grab the frame from the threaded video stream and resize it
		# to have a maximum width of 400 pixels
		#print("obj detection")
		frame = vs.read()
		frame = imutils.resize(frame, width=400)
		# grab the frame dimensions and convert it to a blob
		(h, w) = frame.shape[:2]
		blob = cv2.dnn.blobFromImage(cv2.resize(frame, (300, 300)),
			0.007843, (300, 300), 127.5)
		# pass the blob through the network and obtain the detections and
		# predictions
		net.setInput(blob)
		detections = net.forward()

		# loop over the detections
		for i in np.arange(0, detections.shape[2]):
			# extract the confidence (i.e., probability) associated with
			# the prediction
			confidence = detections[0, 0, i, 2]
			# filter out weak detections by ensuring the `confidence` is
			# greater than the minimum confidence
			if confidence > args["confidence"]:
				# extract the index of the class label from the
				# `detections`, then compute the (x, y)-coordinates of
				# the bounding box for the object
				idx = int(detections[0, 0, i, 1])
				box = detections[0, 0, i, 3:7] * np.array([w, h, w, h])
				(startX, startY, endX, endY) = box.astype("int")
				# draw the prediction on the frame
				label = "{}: {:.2f}%".format(CLASSES[idx],
					confidence * 100)
				cv2.rectangle(frame, (startX, startY), (endX, endY),
					COLORS[idx], 2)
				y = startY - 15 if startY - 15 > 15 else startY + 15
				#print(label)
				data = {
					"object": CLASSES[idx],
					"coords": [str(startX), str(endX)]
				}
				#print(data)
				ret= client1.publish("objectDetection",json.dumps(data),2)

				cv2.putText(frame, label, (startX, y),
					cv2.FONT_HERSHEY_SIMPLEX, 0.5, COLORS[idx], 2)
	fps.update()
    
def take_picture():
	def on_connect(client, userdata, flags, rc):
		print("Connected to takePicture with result code "+str(rc))
		client3.subscribe("takePicture", qos=2)

	def on_message(client, userdata, msg):
		print("Starting to process picture")
		print(msg.payload.decode('utf-8'))
		frame = vs.read()
		name = datetime.now()
		name2= name.strftime("%d-%m-%Y_%H:%M:%S")
		cv2.imwrite("{}.jpg".format(name2), frame) # save frame as JPEG fil
		f=open("{}.jpg".format(name2), "rb") #3.7kiB in same folder
		print("to opening msg")
		strg = ""
		encoded_data = base64.b64encode(f.read())
		for i in xrange((len(encoded_data)/40)+1):
			strg += encoded_data[i*40:(i+1)*40]
		data = {
			"plantID": msg.payload.decode('utf-8'),
			"imageData": str(strg),
			"dateTaken": str(name.strftime("%d-%m-%Y %H:%M:%S"))
		}
		print(data)
		print("before error")
		ret= client1.publish("rpiImage",json.dumps(data),2)
		print(ret.is_published())
	
	broker="localhost"
	port=1883
	client3=paho.Client()
	client3.connect(broker,port)
	client3.on_connect = on_connect
	client3.on_message = on_message
	client3.connect(broker,port)
	while(True):
		client3.loop()


try:
    take_picture_2=threading.Thread(target=take_picture)
    take_picture_2.start()
    obj_detection=threading.Thread(target=object_detection)
    qr_scanner=threading.Thread(target=barcode_scanner)
    obj_detection.start()
    qr_scanner.start()
except KeyboardInterrupt:
    print("E X I T")
    GPIO.cleanup()
    mqtt_subscriber.terminate()
