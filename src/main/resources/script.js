import http from 'k6/http';
import { check, sleep } from 'k6';
import { group } from 'k6';

export const options = {
  scenarios: {
    load_test: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '1m', target: 2500 },
        { duration: '2m', target: 2500 },
        { duration: '30s', target: 0 },
      ],
    },
    endurance_test: {
      executor: 'constant-vus',
      vus: 3000,
      duration: '3m',
      startTime: '4m',
    },
    stress_test: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '1m', target: 3000 },
        { duration: '1m', target: 4000 },
        { duration: '1m', target: 5000 },
        { duration: '30s', target: 0 },
      ],
      startTime: '7m30s',
    },
    peak_test: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '30s', target: 2500 },
        { duration: '30s', target: 5000 },
        { duration: '1m', target: 5000 },
        { duration: '30s', target: 0 },
      ],
      startTime: '11m30s',
    },
  },
};

export default function () {
  group('API Test', function () {
    const res = http.get('http://host.docker.internal:8080/booking/queue/status');
    check(res, { 'status was 200': (r) => r.status === 200 });
    sleep(1);
  });
}