# SmartWateringSystemApp

The Smart Watering System (SWS) is an IoT project used to water multiple plants using an Android App and a hardware controller that consists of a [Raspberry Pi](https://en.wikipedia.org/wiki/Raspberry_Pi), a water pump, [a soil moisture sensor](https://www.sparkfun.com/products/13322), and a relay. An Android App communicates with the Raspberry Pi using [AWS IoT service](https://aws.amazon.com/iot-core/) to control the water pump and to get the soil moisture sensor readings stored in [DynamoDB database](https://aws.amazon.com/dynamodb/). The Smart Watering System can be used to water multiple plants, to set different watering schedules for the plants, to get the watering event history and the soil moisture stat of each plant.

Project Demo: [Demo](https://youtu.be/lDwCmjK6jXQ)

The code for RaspberryPi controller: [plantPiController](https://github.com/richamirashi/plantpicontroller)

&nbsp;

![Architecture](https://github.com/richamirashi/SmartWateringSystemApp/blob/master/SmartWateringSystemArchitecture.PNG)

## Framework:
Software Tools:
  * Frameworks and Libraries:
    a. Frontend: Java for developing an Android App
    b. Backend: Python3 for running a controller on [Raspberry Pi](https://en.wikipedia.org/wiki/Raspberry_Pi), [DynamoDB database](https://aws.amazon.com/dynamodb/), [AWS IoT service](https://aws.amazon.com/iot-core/)
  * SDK: Android SDK
  * IDE and Tools: Android studio 3.0.1
  * Version Control: GitHub
Hardware Tools:
  * [Raspberry Pi 3 Model B](https://www.amazon.com/CanaKit-Raspberry-Premium-Clear-Supply/dp/B07BC7BMHY)
  * 5V /12 V Power supply
  * [5V /12 V Water pump](https://www.amazon.com/gp/product/B07CZ7XFCF)
  * [5V Relay](https://www.amazon.com/gp/product/B00E0NTPP4)
  * [A SparkFun soil moisture sensor](https://www.sparkfun.com/products/13322)
  * [ADC Converter - ADS1115 16 Byte 4 Channel I2C IIC ADC Module](https://www.amazon.com/gp/product/B014KID8ZQ)
  * Flexible water line or Silicone tubing as a water sprinkler
  * Few resistors and a transistor
  * A solderable breadboard
  * A bucket as a water reservoir
  * LED as a second plant
