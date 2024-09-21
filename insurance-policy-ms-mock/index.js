const isEnabled = process.env.ENABLED == 'true' || false;
if (!isEnabled) {
  console.log('### Insurance Policy MS Mock: not enabled.');
  process.exit(0);
}

console.log("#############################################################################");
console.log("### Insurance Policy MS Mock: starting...");
console.log("#############################################################################");

const { Kafka } = require('kafkajs');

const KAFKA_CLIENT_ID = 'insurance-policy-ms-mock';
//const KAFKA_HOST = 'kafka';
const KAFKA_HOST = 'localhost';
const KAFKA_PORT = '9092';
const GROUP_ID = 'insurance';
const RETRIES = 10;
const INITIAL_RETRY_TIME = 3000;
const DELAY_BEFORE_CONNECT_TO_KAFKA = 5000;

const TOPIC_INSURANCE_QUOTE_RECEIVED = 'insurance-quote-received';
const TOPIC_INSURANCE_POLICY_CREATED = 'insurance-policy-created';

const kafka = new Kafka({
  clientId: KAFKA_CLIENT_ID,
  brokers: [KAFKA_HOST + ':' + KAFKA_PORT],
  retry: {
    retries: RETRIES,
    initialRetryTime: INITIAL_RETRY_TIME,
  },
});

const consumer = kafka.consumer({ groupId: GROUP_ID });
const producer = kafka.producer();

// insurancePolicyId random generator
const generatePolicyId = () => Math.floor(Math.random() * 100000);

// delay func
const sleep = (ms) => new Promise(resolve => setTimeout(resolve, ms));

const run = async () => {
  console.log('Waiting a little bit before trying to connect to kafka...');
  await sleep(DELAY_BEFORE_CONNECT_TO_KAFKA);

  await consumer.connect();
  await producer.connect();

  await consumer.subscribe({ topic: TOPIC_INSURANCE_QUOTE_RECEIVED, fromBeginning: true });

  // Consumer listening to the topic
  await consumer.run({
    eachMessage: async ({ topic, partition, message }) => {
      const receivedMessage = JSON.parse(message.value.toString());
      console.log('Msg Received:', receivedMessage);

      const insurancePolicyId = generatePolicyId();
      const newMessage = {
        insuranceQuoteId: receivedMessage.id,
        insurancePolicyId,
      };
      console.log('Publishing msg:', newMessage);

      await producer.send({
        topic: TOPIC_INSURANCE_POLICY_CREATED,
        messages: [{ value: JSON.stringify(newMessage) }],
      });
    },
  });
};

run().catch(console.error);
