import unittest
import urllib2
import urllib
import ssl
import base64
import json

class TestOAuth(unittest.TestCase):

  target = 'https://localhost:8443'
  
  ctx = ssl.create_default_context()
  ctx.check_hostname = False
  ctx.verify_mode = ssl.CERT_NONE

  
  def obtain_token(self):
      data = urllib.urlencode({"grant_type":"client_credentials"})
      url = '%s/oauth-server/j_oauth_token_grant' % self.target
      basicAuth = "Basic %s" % base64.encodestring('test:admin').replace('\n', '')
      req = urllib2.Request(url, data, {
          "Authorization": basicAuth})
      f = urllib2.urlopen(req, context=self.ctx)
      return json.loads(f.read())

  def test_get_plain(self): 
      token = self.obtain_token()
      url = '%s/Service/v1/filter/test/visitLayer' % self.target

      req = urllib2.Request(url, headers={
          "Content-Type": "application/json",
          "Authorization": "Bearer %s" % token['access_token']})
      f = urllib2.urlopen(req, context=self.ctx)

      self.assertEquals(200, f.code)

      body = f.read()
      
      self.assertTrue(len(body) > 0)
      
      bodyJson = json.loads(body)

  def test_get_larger_set(self):
      token = self.obtain_token()

      url = '%s/Service/v1/filter/test/dummy' % self.target

      req = urllib2.Request(url, headers={
          "Content-Type": "application/json",
          "Authorization": "Bearer %s" % token['access_token']})
      f = urllib2.urlopen(req, context=self.ctx)

      self.assertEquals(200, f.code)

      body = f.read()

      self.assertTrue(len(body) > 0)

      bodyJson = json.loads(body)

      self.assertTrue(len(bodyJson['items']) == 100)
      
  def test_get_larger_set_via_pagination(self):
      token = self.obtain_token()

      url = '%s/Service/v1/filter/test/dummy?pageSize=10' % self.target
      items = []
      count = 0
      while (True):
        req = urllib2.Request(url, headers={
            "Content-Type": "application/json",
            "Authorization": "Bearer %s" % token['access_token']})
        f = urllib2.urlopen(req, context=self.ctx)

        self.assertEquals(200, f.code)

        body = f.read()

        self.assertTrue(len(body) > 0)

        bodyJson = json.loads(body)
        items.extend(bodyJson['items'])
        if 'next' in bodyJson['links'] and 'href' in bodyJson['links']['next'] != None and count < 20:
          url = bodyJson['links']['next']['href']
          count += 1
        else:
          break

      self.assertTrue(len(items) == 100)



if __name__ == '__main__':
    unittest.main()
