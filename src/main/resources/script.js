import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
  vus: 1000,
  duration: '10s',
};

export default function () {
  const res = http.get('http://host.docker.internal:8080/booking/queue/status', {
    headers: {
      Authorization: 'Bearer YOUR_TOKEN', // 필요시 Authorization 헤더 추가
    },
  });
  console.log(`Response status: ${res.status}`);
  sleep(1);
}
