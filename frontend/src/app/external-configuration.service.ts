import {Injectable} from '@angular/core';
import {ExternalConfiguration, ExternalConfigurationHandlerInterface} from 'angular6-hal-client';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class ExternalConfigurationService implements ExternalConfigurationHandlerInterface {

    constructor(private http: HttpClient) {
    }

    getProxyUri(): string {
        return 'http://proxy.url/api/';
    }

    getRootUri(): string {
        return 'http://localhost:8080/api/';
    }

    getExternalConfiguration(): ExternalConfiguration {
        return null;
    }

    setExternalConfiguration(externalConfiguration: ExternalConfiguration) {
    }

    deserialize(): any {
    }

    serialize(): any {
    }

    getHttp(): HttpClient {
        return this.http;
    }
}
