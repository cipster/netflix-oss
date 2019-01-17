import * as d3 from 'd3';
import {AfterContentInit, Component} from '@angular/core';

@Component({
    selector: 'grub-structure-chart',
    templateUrl: './structure-chart.component.html',
    styleUrls: ['./structure-chart.component.scss']
})
export class StructureChartComponent implements AfterContentInit {
    private readOnly = false;
    private radius = 100;
    private selectedNode;
    private svgHolder;
    private zoomHolder;
    private documentBody;
    private centerForce;
    private collisionForce;
    private manyBodyForce;
    private zoom;
    private centerX;
    private zoomEnabled;
    private dragEnabled;
    private datasource = {
        nodes: [{id: 1, nodeId: 1, name: 'node1', linkType: 'LINKED', leafNode: true},
            {id: 2, nodeId: 2, name: 'node2', linkType: 'LINKED', leafNode: true}],
        links: [{id: 1, source: 1, target: 2}]
    };

    ngAfterContentInit(): void {
        this.documentBody = d3.select('body');
        this.svgHolder = d3.select('svg');
        this.svgHolder.style('background', 'white');
        this.centerX = this.getCurrentWidth() / 2;
        this.centerForce = d3.forceCenter(this.getCurrentWidth() / 2, this.getCurrentHeight() / 2);
        d3.selection.prototype.appendSVG = function (svgContent) {
            return this.select(function () {
                return this.appendChild(document.importNode(new DOMParser()
                    .parseFromString('<svg xmlns="http://www.w3.org/2000/svg">' + ((typeof svgContent === 'function') ? svgContent.apply(this, arguments) : svgContent) +
                        '</svg>', 'application/xml').documentElement.firstChild, true));
            });
        };
        //popping behaviour for force strength values above 1
        this.collisionForce = d3.forceCollide(100)
            .strength(1);

        this.manyBodyForce = d3.forceManyBody()
            .strength(-100)
            .distanceMin(700)
            .distanceMax(1000);

        this.zoomHolder = this.svgHolder.append('g')
            .attr('class', 'zoom-holder');
        // Zoom
        this.zoom = d3.zoom()
            .scaleExtent([0.5, 3]);

        this.redraw(this.datasource.nodes, this.datasource.links);
    }

    redraw(nodes, links) {
        this.zoomHolder.selectAll('line').remove();
        this.zoomHolder.selectAll('g').remove();
        var linkData = links;
        var nodeData = nodes;
        this.zoomEnabled = false;

        //bounding box
        function boxForce() {
            let currentNode;
            for (let i = 0, n = nodeData.length; i < n; ++i) {
                currentNode = nodeData[i];
                currentNode.x = Math.max(100, Math.min(800 - 100, currentNode.x));
                currentNode.y = Math.max(100, Math.min(400 - 100, currentNode.y));
            }
        }

        let simulation = d3.forceSimulation()
            .force('link', d3.forceLink().id(function (d) {
                return d.id;
            }))
            .force("charge", this.manyBodyForce)
            .force("center_force", this.centerForce)
            .force("box_force", boxForce)
            .force("collide", this.collisionForce);

        // Define the div for the tooltip
        let documentBody = d3.select('body');
        let tooltip = documentBody.append('div')
            .attr('class', 'structure-tooltip')
            .style('display', 'none')
            .style('position', 'absolute');

        documentBody.append('div')
            .attr('class', 'not-onboarded-tooltip')
            .html('<span>This delegate function has not been onboarded yet</span>')
            .style('position', 'absolute');

        documentBody.append('div')
            .attr('class', 'rag-tooltip')
            .html('<span>Delegate Risk</span>')
            .style('position', 'absolute');

        // Define the div for the link tooltip
        var linkTooltip = documentBody.append('div')
            .attr('class', 'structure-link-tooltip')
            .style('display', 'none')
            .style('position', 'absolute');

        let link = this.zoomHolder.append('g')
            .attr('class', 'links')
            .selectAll('line')
            .data(linkData)
            .enter()
            .append('line')
            .style('stroke', 'colorLine');

        let node = this.zoomHolder.append('g')
            .attr('class', 'nodes')
            .selectAll('g')
            .data(nodeData)
            .enter()
            .appendSVG(this.entityTemplate)
            .call(d3.drag()
                .on('start', dragStarted)
                .on('drag', dragged)
                .on('end', dragEnded));

        link.exit().remove();
        node.exit().remove();

        node.on('click', (node: any) => {
            var g = d3.select(node); // The node
            var isSelected = g.classed('selected');
            this.zoomHolder.selectAll('g')
                .classed('selected', false);

            g.classed('selected', !isSelected);
        });

        node.on('contextmenu', (element: any) => {
            d3.event.preventDefault();
            d3.event.stopPropagation();
            tooltip.transition()
                .duration(200)
                .style('display', 'none');
            var g = d3.select(element.target); // The node
            this.zoomHolder.selectAll('g')
                .classed('selected', false);
            g.classed('selected', true);
            tooltip.transition()
                .duration(200)
                .style('display', 'inline-block');
            tooltip.html(this.hoverPopupHtml(element, this.readOnly))
                .style('left', (d3.event.pageX + 10) + 'px')
                .style('top', (d3.event.pageY - 28) + 'px');
            this.selectedNode = element;
        });

        link.on('mouseover', function (d) {
            linkTooltip.transition()
                .duration(200)
                .style('display', 'none');

            var g = d3.select(this); // The link
            g.attr('fill', 'blue');

            linkTooltip.transition()
                .duration(200)
                .style('display', 'inline-block');

            linkTooltip.html(this.hoverLinkPopupHtml(d))
                .style('left', (d3.event.pageX + 10) + 'px')
                .style('top', (d3.event.pageY - 28) + 'px');
        });

        link.on('mouseout', function () {
            linkTooltip.transition()
                .duration(200)
                .style('display', 'none');
        });

        this.svgHolder.on('contextmenu', '*', function () {
            d3.event.preventDefault();
            d3.event.stopPropagation();
        });

        function hideTooltip(tooltip) {
            tooltip.transition()
                .duration(200)
                .style('display', 'none');
        }

        documentBody.on('click', function () {
            hideTooltip(tooltip);
        });

        tooltip.on('click', function () {
            hideTooltip(tooltip);
        });

        tooltip.on('contextmenu', function () {
            d3.event.preventDefault();
            d3.event.stopPropagation();
        });

        node.sort(function (a, b) {
            return a.group < b.group ? -1 : a.group > b.group ? 1 : a.group >= b.group ? 0 : NaN;
        });

        simulation
            .nodes(this.datasource.nodes)
            .on('tick', ticked);

        simulation.force('link')
            .links(this.datasource.links)
            .distance(200);

        simulation.restart();

        function ticked() {
            link
                .attr('x1', function (link) {
                    return link.source.x + 44;
                })
                .attr('y1', function (link) {
                    return link.source.y + 44;
                })
                .attr('x2', function (link) {
                    return link.target.x + 44;
                })
                .attr('y2', function (link) {
                    return link.target.y + 44;
                });
            node
                .attr('transform', function (nodeData) {
                    var xValue = nodeData.fx ? nodeData.fx : nodeData.x;
                    var yValue = nodeData.fy ? nodeData.fy : nodeData.y;
                    nodeData.fx = nodeData.x;
                    nodeData.fy = nodeData.y;
                    return 'translate(' + xValue + ',' + yValue + ')';
                });

        }

        function dragStarted(nodeData) {
            // if (!this.dragEnabled) {
            //     return;
            // }
            if (!d3.event.active) {
                simulation.alphaTarget(0.3).restart();
            }
            nodeData.fx = nodeData.x;
            nodeData.fy = nodeData.y;
        }

        function dragged(nodeData) {
            // if (!this.dragEnabled) {
            //     return;
            // }
            nodeData.fx = d3.event.x;
            nodeData.fy = d3.event.y;
        }

        function dragEnded(nodeData) {
            // if (!this.dragEnabled) {
            //     return;
            // }
            if (!d3.event.active) {
                simulation.alphaTarget(0);
            }

            // this.saveNodePosition(angular.copy(nodeData));
        }

        function setZoomWatcher() {
            // $scope.$watch(function () {
            //     return ctrl.zoomEnabled;
            // }, function (newValue, oldValue) {
            //     if (newValue === true && newValue !== oldValue) {
            //         svgHolder.call(zoom
            //             .on('zoom', zoomCallback));
            //     } else {
            //         svgHolder.call(zoom
            //             .on('zoom', null));
            //     }
            // });
        }

        setZoomWatcher();
    };

    colorLine = function () {
        return '#0F0F0F';
    };

    hoverPopupHtml = function (data, readOnly) {
        var removeFromFund = '';
        var linkFunction = '';
        var linkParty = '';
        var viewEntity = '';
        if (data.linkType !== 'LINKED_AS_FUND' && data.leafNode === true) {
            if (data.linkType !== 'LINKED_AS_DELEGATE_FUNCTION') {
                removeFromFund = '' +
                    '<div class="item remove-party" data-rmparty-id="' + data.id + '">';
            } else {
                removeFromFund = '' +
                    '<div class="item remove-function" data-rmfunction-id="' + -data.id + '">';
            }
            removeFromFund +=
                '            <i class="trash middle aligned icon"></i>' +
                '            <div class="content">' +
                '                <div class="header">' +
                '                    Remove from fund' +
                '                </div>' +
                '            </div>' +
                '        </div>';
        }
        if (data.linkType === 'LINKED_AS_FUND') {
            linkFunction = '' +
                '        <div class="item link-delegate-function-trigger">' +
                '            <i class="linkify middle aligned icon"></i>' +
                '            <div class="content">' +
                '                <div class="header">' +
                '                    Link a delegate function' +
                '                </div>' +
                '            </div>' +
                '        </div>';
        }
        if (data.linkType !== 'LINKED_AS_DELEGATE_FUNCTION') {
            linkParty = '' +
                '        <div class="item link-party-trigger" data-cp-id="' + data.id + '">' +
                '            <i class="linkify middle aligned icon"></i>' +
                '            <div class="content">' +
                '                <div class="header">' +
                '                    Link a party' +
                '                </div>' +
                '            </div>' +
                '        </div>';
            viewEntity = '' +
                '        <a class="item" ui-sref="private.party({id:' + data.id + '})">' +
                '            <i class="eye middle aligned icon"></i>' +
                '            <div class="content">' +
                '                <div class="middle aligned header">' +
                '                    View this party' +
                '                </div>' +
                '            </div>' +
                '        </a>';
        } else {
            viewEntity = '' +
                '        <a class="item" ui-sref="private.DELEGATE({id:' + data.delegateId + ', tab: \'' + data.delegateFunctionType + '\'})">' +
                '            <i class="eye middle aligned icon"></i>' +
                '            <div class="content">' +
                '                <div class="middle aligned header">' +
                '                    View this delegate' +
                '                </div>' +
                '            </div>' +
                '        </a>';
        }
        var template =
            '<div oncontextmenu="false">' +
            '    <div class="ui list divided action-list">' +
            viewEntity + (
                readOnly ? '' : (linkParty + linkFunction + removeFromFund)) +
            '    </div>' +
            '</div>';

        return template;
    };


    private entityTemplate = function (data) {
        if (data.linkType === 'LINKED_AS_FUND') {
            data.name = this.fundName;
            data.type = 'FUND';
        }
        if (data.linkType === 'LINKED_AS_DELEGATE_FUNCTION') {
            data.type = 'DELEGATE_FUNCTION';
        }
        var tmpl =
            '<g opacity="1" fill="#000000" fill-rule="nonzero">' +
            '<text x="44" y="-5" text-anchor="middle" style="font-weight:bold;font-size:16px;fill:#333333;position:relative">' +
            data.name +
            '</text>';

        function getColorFromType(data: any) {
            return "";
        }

        if (data.type === 'FUND') {
            tmpl += '<g class="hoverable"><rect height="85" width="85" style="fill:#ffffff;fill-rule:evenodd;stroke:#e8e8e8;stroke-width:3px;"></rect>' +
                '<path style="opacity:1;fill:grey;fill-rule:nonzero;" transform="translate(28,24)" d="m 30.114309,5.9285288 c 0.396043,0 0.717144,-0.3210971 0.717144,-0.7171443 V 0.90851811 c 0,-0.39604806 -0.321101,-0.71714515 -0.717144,-0.71714515 h -4.302867 c -0.396042,0 -0.717144,0.32109709 -0.717144,0.71714515 V 1.6256625 H 5.9704478 V 0.90851811 c 0,-0.39604806 -0.3211013,-0.71714515 -0.7171443,-0.71714515 H 0.9504372 c -0.39604299,0 -0.71714437,0.32109709 -0.71714437,0.71714515 V 5.2113845 c 0,0.3960472 0.32110138,0.7171443 0.71714437,0.7171443 H 1.6675815 V 21.227609 H 0.9504372 c -0.39604299,0 -0.71714437,0.321096 -0.71714437,0.717144 v 4.302865 c 0,0.396048 0.32110138,0.717145 0.71714437,0.717145 h 4.3028663 c 0.396043,0 0.7171443,-0.321097 0.7171443,-0.717145 V 25.530474 H 25.094298 v 0.717144 c 0,0.396048 0.321102,0.717145 0.717144,0.717145 h 4.302867 c 0.396043,0 0.717144,-0.321097 0.717144,-0.717145 v -4.302865 c 0,-0.396048 -0.321101,-0.717144 -0.717144,-0.717144 H 29.397164 V 5.9285288 Z m -3.107626,-3.82477 h 1.912385 v 1.9123849 h -1.912385 z m -24.8610052,0 H 4.0580627 V 4.0161437 H 2.1456778 Z M 4.0580627,25.052378 H 2.1456778 v -1.912384 h 1.9123849 z m 24.8610053,0 h -1.912385 v -1.912384 h 1.912385 z m -2.390482,-3.824769 h -0.717144 c -0.396042,0 -0.717144,0.321096 -0.717144,0.717144 v 0.717144 H 5.9704478 v -0.717144 c 0,-0.396048 -0.3211013,-0.717144 -0.7171443,-0.717144 H 4.536159 V 5.9285288 h 0.7171445 c 0.396043,0 0.7171443,-0.3210971 0.7171443,-0.7171443 V 4.49424 H 25.094298 v 0.7171445 c 0,0.3960472 0.321102,0.7171443 0.717144,0.7171443 h 0.717144 z M 24.377153,9.7532988 h -5.02001 V 6.6456732 c 0,-0.3961086 -0.321101,-0.7171444 -0.717144,-0.7171444 H 6.6875922 c -0.396043,0 -0.7171444,0.3210358 -0.7171444,0.7171444 V 16.685694 c 0,0.396108 0.3211014,0.717144 0.7171444,0.717144 h 5.0200108 v 3.107626 c 0,0.396109 0.321102,0.717145 0.717145,0.717145 h 11.952405 c 0.396043,0 0.717145,-0.321036 0.717145,-0.717145 V 10.470443 c 0,-0.396108 -0.321102,-0.7171442 -0.717145,-0.7171442 z M 8.3609295,8.3190101 H 16.966662 V 15.012358 H 8.3609295 Z M 22.703817,18.837127 h -8.605733 v -1.434289 h 4.541915 c 0.396043,0 0.717144,-0.321036 0.717144,-0.717144 V 12.14378 h 3.346674 z"></path>' +
                '<rect height="4" width="30" x="28" y="60" style="fill-rule:nonzero;opacity:1;fill:' + getColorFromType(data) + ';"></rect>' +
                '<rect height="85" width="85" style="fill:transparent;"></rect></g>';
        } else if (data.type === 'DELEGATE_FUNCTION') {
            tmpl += '<g class="hoverable"><rect height="85" width="85" style="fill:#ffffff;fill-rule:evenodd;stroke:#e8e8e8;stroke-width:3px;"></rect>' +
                '<path style="opacity:1;fill:grey;fill-rule:nonzero;" transform="translate(28,24)" d="M 28.421301,0.06779601 H 2.7070218 c -1.4196427,0 -2.57142858,1.15178889 -2.57142858,2.57143319 V 3.4963787 H 30.99273 V 2.6392292 c 0,-1.4196443 -1.151787,-2.57143319 -2.571429,-2.57143319 z M 0.13559322,21.496363 c 0,1.419644 1.15178588,2.571433 2.57142858,2.571433 H 28.421301 c 1.419642,0 2.571429,-1.151789 2.571429,-2.571433 V 5.2106622 H 0.13559322 Z M 18.992732,9.0678041 c 0,-0.2357176 0.192857,-0.4285748 0.428571,-0.4285748 h 7.714284 c 0.235715,0 0.428572,0.1928572 0.428572,0.4285748 v 0.8571341 c 0,0.2357178 -0.192857,0.4285748 -0.428572,0.4285748 h -7.714284 c -0.235714,0 -0.428571,-0.192857 -0.428571,-0.4285748 z m 0,3.4285669 c 0,-0.235717 0.192857,-0.428574 0.428571,-0.428574 h 7.714284 c 0.235715,0 0.428572,0.192857 0.428572,0.428574 v 0.857149 c 0,0.235703 -0.192857,0.42856 -0.428572,0.42856 h -7.714284 c -0.235714,0 -0.428571,-0.192857 -0.428571,-0.42856 z m 0,3.428567 c 0,-0.235717 0.192857,-0.428574 0.428571,-0.428574 h 7.714284 c 0.235715,0 0.428572,0.192857 0.428572,0.428574 v 0.857149 c 0,0.235718 -0.192857,0.428575 -0.428572,0.428575 h -7.714284 c -0.235714,0 -0.428571,-0.192857 -0.428571,-0.428575 z M 9.5641623,8.6392293 c 1.8910707,0 3.4285707,1.5375027 3.4285707,3.4285677 0,1.891079 -1.5375,3.428567 -3.4285707,3.428567 -1.8910704,0 -3.4285703,-1.537488 -3.4285703,-3.428567 0,-1.891065 1.5374999,-3.4285677 3.4285703,-3.4285677 z M 3.7302352,19.578514 c 0.4500003,-1.376783 1.7410706,-2.367852 3.2624986,-2.367852 h 0.4392868 c 0.6589279,0.273209 1.3767852,0.428559 2.1321417,0.428559 0.7553567,0 1.4785707,-0.15535 2.1321417,-0.428559 h 0.439287 c 1.521428,0 2.812499,0.991069 3.2625,2.367852 0.171427,0.530358 -0.278573,1.060715 -0.835715,1.060715 H 4.5659497 c -0.5571426,0 -1.0071429,-0.535711 -0.8357145,-1.060715 z"></path>' +
                '<rect height="4" width="30" x="28" y="60" style="fill-rule:nonzero;opacity:1;fill:' + getColorFromType(data) + ';"></rect>' +
                '<rect height="85" width="85" style="fill:transparent;"></rect></g>';
        } else if (data.type === 'INDIVIDUAL') {
            tmpl += '<g class="hoverable"><rect height="88" width="88" rx="44" style="fill:#ffffff;fill-rule:evenodd;stroke:#e8e8e8;stroke-width:2px;"></rect>' +
                '<path style="opacity:1;fill:grey;fill-rule:nonzero;" transform="translate(34,30)" d="M11,13.480469 C13.109375,13.480469 15.306641,13.939453 17.591797,14.857422 C19.876953,15.775391 21.019531,16.976562 21.019531,18.460938 L21.019531,20.980469 L0.980469,20.980469 L0.980469,18.460938 C0.980469,16.976562 2.123047,15.775391 4.408203,14.857422 C6.693359,13.939453 8.890625,13.480469 11,13.480469 Z M11,10.960938 C9.632812,10.960938 8.460938,10.472656 7.484375,9.496094 C6.507812,8.519531 6.019531,7.347656 6.019531,5.980469 C6.019531,4.613281 6.507812,3.431641 7.484375,2.435547 C8.460938,1.439453 9.632812,0.941406 11,0.941406 C12.367188,0.941406 13.539062,1.439453 14.515625,2.435547 C15.492188,3.431641 15.980469,4.613281 15.980469,5.980469 C15.980469,7.347656 15.492188,8.519531 14.515625,9.496094 C13.539062,10.472656 12.367188,10.960938 11,10.960938 Z"></path>' +
                '<rect height="4" width="22" x="34" y="57" style="fill-rule:nonzero;opacity:1;fill:' + getColorFromType(data) + ';"></rect>' +
                '<rect height="88" width="88" rx="44" style="fill:transparent;"></rect></g>';
        } else {
            tmpl += '<g class="hoverable"><rect height="85" width="85" style="fill:#ffffff;fill-rule:evenodd;stroke:#e8e8e8;stroke-width:2px;"></rect>' +
                '<path style="opacity:1;fill:grey;fill-rule:nonzero;" transform="translate(32,30)" d="m 20.5,15.25 v 2.519531 H 17.980469 V 15.25 Z m 0,-4.980469 v 2.460938 h -2.519531 v -2.460938 z m 2.519531,9.960938 V 7.75 H 13 v 2.519531 h 2.519531 v 2.460938 H 13 V 15.25 h 2.519531 v 2.519531 H 13 v 2.460938 z m -12.539062,-15 V 2.769531 H 8.019531 v 2.460938 z m 0,5.039062 V 7.75 H 8.019531 v 2.519531 z m 0,4.980469 V 12.730469 H 8.019531 V 15.25 Z m 0,4.980469 V 17.769531 H 8.019531 v 2.460938 z M 5.5,5.230469 V 2.769531 H 2.980469 v 2.460938 z m 0,5.039062 V 7.75 H 2.980469 v 2.519531 z M 5.5,15.25 V 12.730469 H 2.980469 V 15.25 Z m 0,4.980469 V 17.769531 H 2.980469 v 2.460938 z m 7.5,-15 H 25.480469 V 22.75 H 0.519531 V 0.25 H 13 Z"></path>' +
                '<rect height="4" width="26" x="32" y="60" style="fill-rule:nonzero;opacity:1;fill:' + getColorFromType(data) + ';"></rect>' +
                '<rect height="85" width="85" style="fill:transparent;"></rect></g>';
        }

        if (data.linkType === 'LINKED_AS_DELEGATE_FUNCTION' && data.valid === false) {
            tmpl += '<g transform="translate(75,0)">' +
                '<path style="fill:#f95b6e;fill-rule:nonzero" d="M 16.875,0 C 18.625,0 20,1.375 20,3.125 V 13.75 c 0,1.75 -1.375,3.125 -3.125,3.125 H 11.5625 L 7.9375,20 H 6.875 V 16.875 H 3.125 C 1.375,16.875 0,15.5 0,13.75 V 3.125 C 0,1.375 1.375,0 3.125,0 Z M 18.75,13.75 V 3.125 C 18.75,2.0625 17.9375,1.25 16.875,1.25 H 3.125 C 2.0625,1.25 1.25,2.0625 1.25,3.125 V 13.75 c 0,1.0625 0.8125,1.875 1.875,1.875 h 5 v 2.5625 l 2.9375,-2.5625 h 5.8125 c 1.0625,0 1.875,-0.8125 1.875,-1.875 z M 9,11 h 1 v 1 H 9 Z M 9,5 h 1 V 9 H 9 Z"></path>' +
                '<circle r="15" transform="translate(10,10)" style="fill:transparent;cursor:pointer;" class="document-management-icon"></circle>' +
                '</g>';
        }
        if (data.linkType === 'LINKED_AS_DELEGATE_FUNCTION' && data.valid === true && data.rag) {
            tmpl += '<g transform="translate(-10,0)">' +
                '<circle r="10" transform="translate(10,10)" style="fill:' + this.getRagColor(data.rag) + ';cursor:pointer;" class="rag-icon"></circle>' +
                '</g>';
        }

        if (data.countryOfResidence) {
            tmpl += '<text x="44" y="105" text-anchor="middle" style="font-weight:bold;font-size:14px;opacity:0.4;fill:#333333;">' +
                data.countryOfResidence +
                '</text>';
        }

        if (data.delegateFunctionType) {
            tmpl += '<text x="44" y="120" text-anchor="middle" style="font-weight:bold;font-size:14px;opacity:0.4;fill:#333333;">' +
                data.delegateFunctionType +
                '</text>';
        }

        tmpl += '</g>';

        return tmpl;
    };

    private getRagColor(rag: any) {
        return "";
    }

    private getCurrentHeight() {
        return 500;
    }

    private getCurrentWidth() {
        return 500;
    }


}
