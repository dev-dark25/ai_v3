import service from '@/axios'

export function call(param: object) {
  return service({
    url: '/api/call',
    method: 'post',
    data: param,
  })
}

export function logout() {
  return service({
    url: '/api/logout',
    method: 'post',
    // data: param,
  })
}
