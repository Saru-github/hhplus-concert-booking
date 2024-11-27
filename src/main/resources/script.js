import http from 'k6/http';
import { check, sleep } from 'k6';
import { group } from 'k6';

// // 1. Load Test
// export const options = {
//   scenarios: {
//     load_test: {
//       executor: 'ramping-vus',
//       startVUs: 0,
//       stages: [
//         { duration: '1m', target: 1500 },
//         { duration: '1m', target: 1500 },
//         { duration: '30s', target: 0 },
//       ],
//     }
//   },
// };

// 2.Endurance Test
export const options = {
  scenarios: {
    endurance_test: {
      executor: 'constant-vus',
      vus: 2000,
      duration: '5m',
    }
  },
};

// // 3. Stress Test
// export const options = {
//   scenarios: {
//     stress_test: {
//       executor: 'ramping-vus',
//       startVUs: 0,
//       stages: [
//         { duration: '30s', target: 2000 },
//         { duration: '30s', target: 2500 },
//         { duration: '30s', target: 3000 },
//         { duration: '30s', target: 0 },
//       ],
//     }
//   },
// };
//
// export const options = {
//   scenarios: {
//     peak_test: {
//       executor: 'ramping-vus',
//       startVUs: 0,
//       stages: [
//         { duration: '20s', target: 1500 },
//         { duration: '20s', target: 3000 },
//         { duration: '40s', target: 3000 },
//         { duration: '20s', target: 0 },
//       ],
//     }
//   },
// };
//
// // 4. Peak Test
// export default function () {
//   const res = http.get('http://host.docker.internal:8080/booking/queue/status');
//   check(res, { 'status was 200': (r) => r.status === 200 });
//   sleep(1);
// }

export default function () {
  group('API Test', function () {
    const res = http.get('http://host.docker.internal:8080/booking/queue/status');
    check(res, { 'status was 200': (r) => r.status === 200 });
    sleep(1);
  });
}